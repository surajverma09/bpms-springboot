package com.suraj.bpms.project;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.exception.NoEligibleUserIsFoundException;
import com.suraj.bpms.project.repository.*;
import com.suraj.bpms.project.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                        TaskActionType.APPROVE
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
}
