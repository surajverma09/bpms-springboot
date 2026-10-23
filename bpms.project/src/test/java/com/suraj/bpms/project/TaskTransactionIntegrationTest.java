package com.suraj.bpms.project;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.exception.NoEligibleUserIsFoundException;
import com.suraj.bpms.project.repository.*;
import com.suraj.bpms.project.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskTransactionIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProcessStepRepository processStepRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProcessDefinitionRepository processDefinitionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    @Test
    void shouldRollbackWhenNoEligibleUserExists() {
        Role managerRole = new Role();
        managerRole.setName("MANAGER");
        managerRole = roleRepository.save(managerRole);

        Role financeRole = new Role();
        financeRole.setName("FINANCE");
        financeRole = roleRepository.save(financeRole);

        User manager = new User();
        manager.setName("Test Manager");
        manager.setEmail("manager.rollback-test@example.com");
        manager.setPassword("test-password-placeholder");
        manager.setRole(managerRole);
        manager.setIsDeleted(false);

        manager = userRepository.save(manager);

        ProcessDefinition processDefinition = new ProcessDefinition();

        processDefinition.setName("Rollback Test Workflow");
        processDefinition.setDescription(
                "Testing rollback when no eligible user exists"
        );
        processDefinition.setVersion(1);
        processDefinition.setCreatedBy(manager);

        processDefinition =
                processDefinitionRepository.save(processDefinition);

        ProcessStep managerStep = new ProcessStep();
        managerStep.setProcessDefinition(processDefinition);
        managerStep.setAssignedRole(managerRole);
        managerStep.setStepName("Manager Approval");
        managerStep.setStepOrder(1);
        managerStep = processStepRepository.save(managerStep);

        ProcessStep financeStep = new ProcessStep();
        financeStep.setProcessDefinition(processDefinition);
        financeStep.setAssignedRole(financeRole);
        financeStep.setStepName("Finance Approval");
        financeStep.setStepOrder(2);
        financeStep = processStepRepository.save(financeStep);

        ProcessInstance processInstance = new ProcessInstance();

        processInstance.setProcessDefinition(processDefinition);
        processInstance.setStartedBy(manager);
        processInstance.setRequestData("{\"purpose\":\"rollback-test\"}");
        processInstance.setStatus("RUNNING");

        processInstance = processInstanceRepository.save(processInstance);

        Task currentTask = new Task();

        currentTask.setProcessInstance(processInstance);
        currentTask.setProcessStep(managerStep);
        currentTask.setAssignedToUser(manager);
        currentTask.setStatus("PENDING");

        currentTask = taskRepository.save(currentTask);

        Long taskId = currentTask.getId();
        Long managerId = manager.getId();

        assertThrows(
                NoEligibleUserIsFoundException.class,
                () -> taskService.approveOrReject(
                        taskId,
                        managerId,
                        TaskActionType.APPROVE, null
                )
        );

        Task taskAfterFailure = taskRepository.findById(taskId)
                .orElseThrow();

        ProcessInstance instanceAfterFailure =
                processInstanceRepository.findById(processInstance.getId())
                        .orElseThrow();

        assertEquals("PENDING", taskAfterFailure.getStatus());
        assertEquals("RUNNING", instanceAfterFailure.getStatus());
    }
    @Test
    void shouldAllowOnlyOneConcurrentApproval() throws Exception {
        Role managerRole = roleRepository.findByName("MANAGER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("MANAGER");
                    return roleRepository.save(role);
                });

        Role financeRole = roleRepository.findByName("FINANCE")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("FINANCE");
                    return roleRepository.save(role);
                });

        User manager = new User();
        manager.setRole(managerRole);
        manager.setEmail("Test2@gmail.com");
        manager.setIsDeleted(false);
        manager.setPassword("test-password-placeholder");
        manager.setName("Test manager");

        manager = userRepository.save(manager);

        User finance = new User();
        finance.setName("Test financer");
        finance.setPassword("test-password-placeholder2");
        finance.setEmail("Test3@gmail.com");
        finance.setRole(financeRole);
        finance.setIsDeleted(false);

        finance = userRepository.save(finance);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setName("Concurrent approval workflow");
        processDefinition.setDescription("lock 2nd task while approving 1st task");
        processDefinition.setVersion(1);
        processDefinition.setCreatedBy(manager);

        processDefinition = processDefinitionRepository.save(processDefinition);

        ProcessStep managerStep = new ProcessStep();
        managerStep.setProcessDefinition(processDefinition);
        managerStep.setAssignedRole(managerRole);
        managerStep.setStepName("Manager Approval");
        managerStep.setStepOrder(1);
        managerStep = processStepRepository.save(managerStep);

        ProcessStep financeStep = new ProcessStep();
        financeStep.setProcessDefinition(processDefinition);
        financeStep.setAssignedRole(financeRole);
        financeStep.setStepName("Finance Approval");
        financeStep.setStepOrder(2);
        financeStep = processStepRepository.save(financeStep);

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setProcessDefinition(processDefinition);
        processInstance.setStatus("RUNNING");
        processInstance.setRequestData("{\"purpose\":\"concurrency-test\"}");
        processInstance.setStartedBy(manager);

        processInstance = processInstanceRepository.save(processInstance);

        Task task = new Task();
        task.setAssignedToUser(manager);
        task.setStatus("PENDING");
        task.setProcessInstance(processInstance);
        task.setProcessStep(managerStep);

        task = taskRepository.save(task);

        Long taskId = task.getId();
        Long managerId = manager.getId();

        Long processInstanceId = processInstance.getId();
        Long financeStepId = financeStep.getId();


        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<String> approvalAttempt = () -> {
            ready.countDown();
            start.await();

            try{
                taskService.approveOrReject(
                        taskId,
                        managerId,
                        TaskActionType.APPROVE,
                        null
                );
                return "SUCCESS";
            }
            catch (Exception e){
                return "REJECTED";
            }
        };



        Future<String> resultA = executor.submit(approvalAttempt);
        Future<String> resultB = executor.submit(approvalAttempt);

        assertTrue(ready.await(5, TimeUnit.SECONDS));

        start.countDown();

        List<String> results = List.of(
                resultA.get(10, TimeUnit.SECONDS),
                resultB.get(10, TimeUnit.SECONDS)
        );

        executor.shutdown();

        assertEquals(1, results.stream()
                .filter("SUCCESS"::equals)
                .count());

        assertEquals(1, results.stream()
                .filter("REJECTED"::equals)
                .count());

        Task savedManagerTask = taskRepository.findById(taskId)
                .orElseThrow();

        assertEquals("APPROVED", savedManagerTask.getStatus());

        long financeTaskCount = taskRepository.findAll()
                .stream()
                .filter(t -> t.getProcessInstance().getId().equals(processInstanceId))
                .filter(t -> t.getProcessStep().getId().equals(financeStepId)).count();

        assertEquals(1L, financeTaskCount);


        ProcessInstance savedProcessInstance = processInstanceRepository.findById(processInstanceId)
                .orElseThrow();

        assertEquals("RUNNING", savedProcessInstance.getStatus());
    }
}
