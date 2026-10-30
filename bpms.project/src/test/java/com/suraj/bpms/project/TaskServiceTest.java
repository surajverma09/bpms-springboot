package com.suraj.bpms.project;

import com.suraj.bpms.project.dto.auditlog.AuditLogCreateDTO;
import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.mapper.TaskMapper;
import com.suraj.bpms.project.repository.*;
import com.suraj.bpms.project.service.AuditLogService;
import com.suraj.bpms.project.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ProcessStepRepository processStepRepository;
    @Mock
    TaskActionRepository taskActionRepository;
    @Mock
    private AuditLogService auditLogService;
    @Mock
    private TaskMapper taskMapper;


    @InjectMocks
    TaskService taskService;


        @Test //test -> 1
        void shouldThrowAnExceptionWhenTaskIsAlreadyApproved(){

            Task task = new Task();
            task.setId(21L);
            task.setStatus("APPROVED");

            ProcessInstance processInstance = new ProcessInstance();

            ProcessDefinition processDefinition = new ProcessDefinition();
            processDefinition.setName("Leave Application");

            processInstance.setProcessDefinition(processDefinition);

            task.setProcessInstance(processInstance);

            User user = new User();


            Role role = new Role();
            role.setName("HR");


            user.setRole(role);

            when(taskRepository.findByIdForUpdate(21L)).
                    thenReturn(Optional.of(task));

            when(userRepository.findByEmailAndIsDeletedFalse("hr@gmail.com"))
                    .thenReturn(Optional.of(user));

            assertThrows(
                    InvalidTaskStatusException.class,
                    ()->taskService.approveOrReject(
                            21L,"hr@gmail.com", TaskActionType.APPROVE,null
                    )
            );
        }
    @Test //test -> 2
    void shouldThrowExceptionWhenUserIsNotAuthorisedOrAssignedToTask(){

        Task task = new Task();
        User user = new User();

        task.setStatus("PENDING");
        user.setId(10L);
        user.setEmail("manager@gmail.com");
        task.setAssignedToUser(user);

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setName("Leave Application");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setProcessDefinition(processDefinition);

        task.setProcessInstance(processInstance);
        Role role = new Role();
        role.setName("EMPLOYEE");

        User requestingUser = new User();
        requestingUser.setEmail("manager@gmail.com");
        requestingUser.setId(20L);
        requestingUser.setRole(role);

        when(userRepository.findByEmailAndIsDeletedFalse("manager@gmail.com"))
                .thenReturn(Optional.of(requestingUser));

        when(taskRepository.findByIdForUpdate(21L)).thenReturn(Optional.of(task));

        assertThrows(UnauthorizedUserFoundException.class,
                ()->taskService.approveOrReject
                        (21L, "manager@gmail.com", TaskActionType.APPROVE, null));
    }

    @Test //test -> 3
    void shouldThrowExceptionWhenTaskIsNotFound(){

        when(taskRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,()-> taskService.approveOrReject
                (99L, "hr@gmail.com", TaskActionType.APPROVE, null));
    }

    @Test //test -> 4
    void shouldThrowExceptionWhenTaskActionIsNotFound(){

        User user = new User();
        Role role = new Role();
        role.setName("MANAGER");
        user.setRole(role);

//        when(userRepository.findByEmailAndIsDeletedFalse("manager@gmail.com"))
//                .thenReturn(Optional.of(user));


        Task task = new Task();
        task.setStatus("PENDING");
        user.setId(10L);
        user.setEmail("manager@gmail.com");
        task.setAssignedToUser(user);
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setName("Leave Application");

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setProcessDefinition(processDefinition);

        task.setProcessInstance(processInstance);
//        when(taskRepository.findByIdForUpdate(21L)).thenReturn(Optional.of(task));

        assertThrows(InvalidTaskActionException.class,()->
                taskService.approveOrReject(21L, "manager@gmail.com", null, null));
    }

    @Test //test -> 5
    void shouldRejectProcessInstanceWhenUserRejectTask(){

        ProcessInstance processInstance = new ProcessInstance();
        Task task = new Task();
        User user = new User();
        Role role = new Role();

        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setName("Leave Application");

        processInstance.setId(100L);
        processInstance.setProcessDefinition(processDefinition);

        user.setId(10L);
        user.setEmail("manager@gmail.com");
        user.setRole(role);
        role.setName("MANAGER");

        task.setId(21L);
        task.setProcessInstance(processInstance);
        task.setAssignedToUser(user);
        task.setStatus("PENDING");

        when(userRepository.findByEmailAndIsDeletedFalse("manager@gmail.com"))
                .thenReturn(Optional.of(user));


        when(taskRepository.findByIdForUpdate(21L)).thenReturn(Optional.of(task));

        when(userRepository.findByIdAndIsDeletedFalse(10L)).thenReturn(Optional.of(user));


        taskService.approveOrReject(21L, "manager@gmail.com", TaskActionType.REJECT, null);

        ArgumentCaptor<TaskAction> captor = ArgumentCaptor.forClass(TaskAction.class);

        verify(taskActionRepository).save(captor.capture());

        TaskAction savedAction = captor.getValue();

        assertEquals("REJECT", savedAction.getAction());
        assertEquals(task, savedAction.getTask());
        assertEquals(user, savedAction.getActionBy());

        assertEquals("REJECTED", task.getStatus());
        assertEquals("REJECTED", processInstance.getStatus());
        assertNotNull(task.getCompletedAt());


        ArgumentCaptor<AuditLogCreateDTO> auditLog = ArgumentCaptor.forClass(AuditLogCreateDTO.class);

        verify(auditLogService, times(2)).createAuditLog(auditLog.capture());

        List<AuditLogCreateDTO> audits = auditLog.getAllValues();

        assertEquals("TASK_REJECTED", audits.get(0).getAction());
        assertEquals("TASK", audits.get(0).getEntityType());
        assertEquals(task.getId(), audits.get(0).getEntityId());

        assertEquals("PROCESS_INSTANCE_REJECTED", audits.get(1).getAction());
        assertEquals("PROCESS_INSTANCE", audits.get(1).getEntityType());
        assertEquals(task.getProcessInstance().getId(), audits.get(1).getEntityId());

    }

    @Test
    void shouldCompleteProcessInstanceWhenDirectorApprovesIt(){
        User user = new User();
        Role role = new Role();
        role.setName("DIRECTOR");
        user.setRole(role);
        ProcessInstance processInstance = new ProcessInstance();
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setName("Leave Application");
        processInstance.setProcessDefinition(processDefinition);
        ProcessStep processStep = new ProcessStep();
        Task task = new Task();

        when(userRepository.findByIdAndIsDeletedFalse(10L))
                .thenReturn(Optional.of(user));


        when(userRepository.findByEmailAndIsDeletedFalse("manager@gmail.com"))
                .thenReturn(Optional.of(user));


        processStep.setProcessDefinition(processDefinition);
        processStep.setStepOrder(3);
        processInstance.setStatus("RUNNING");
        task.setStatus("PENDING");
        task.setProcessInstance(processInstance);
        task.setProcessStep(processStep);
        user.setId(10L);
        user.setEmail("manager@gmail.com");
        task.setAssignedToUser(user);
        task.setId(21L);
        processInstance.setId(100L);

        when(taskRepository.findByIdForUpdate(21L)).thenReturn(Optional.of(task));

        when(processStepRepository
                .findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinition.getId(),processStep.getStepOrder()))
                .thenReturn(Optional.empty());

        taskService.approveOrReject(21L, "manager@gmail.com", TaskActionType.APPROVE, null);

        ArgumentCaptor<TaskAction> captor = ArgumentCaptor.forClass(TaskAction.class);

        verify(taskActionRepository).save(captor.capture());

        TaskAction savedAction = captor.getValue();

        assertEquals("APPROVE", savedAction.getAction());
        assertEquals(task, savedAction.getTask());
        assertEquals(user , savedAction.getActionBy());

        assertEquals("APPROVED", task.getStatus());
        assertEquals("COMPLETED", processInstance.getStatus());
        assertNotNull(task.getCompletedAt());

        verify(taskRepository, times(1)).save(task);

        ArgumentCaptor<AuditLogCreateDTO> captorLog = ArgumentCaptor.forClass(AuditLogCreateDTO.class);

        verify(auditLogService, times(2)).createAuditLog(captorLog.capture());

        List<AuditLogCreateDTO> audits = captorLog.getAllValues();

        assertEquals("TASK_APPROVED", audits.get(0).getAction());
        assertEquals("TASK", audits.get(0).getEntityType());
        assertEquals(task.getId(), audits.get(0).getEntityId());

        assertEquals("PROCESS_INSTANCE_COMPLETED", audits.get(1).getAction());
        assertEquals("PROCESS_INSTANCE", audits.get(1).getEntityType());
        assertEquals(task.getProcessInstance().getId(), audits.get(1).getEntityId());
    }

    @Test
    void processInstanceShouldRemainRunningWhenFirstTaskIsApproved(){
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        User currentUser = new User();
        User nextUser = new User();
        ProcessDefinition processDefinition = new ProcessDefinition();
        ProcessStep currentStep = new ProcessStep();
        ProcessStep nextStep = new ProcessStep();
        ProcessInstance processInstance = new ProcessInstance();
        Task task = new Task();
        Role managerRole = new Role();
        Role financeRole = new Role();

        when(userRepository.findByIdAndIsDeletedFalse(10L))
                .thenReturn(Optional.of(currentUser));

        when(userRepository.findByEmailAndIsDeletedFalse("manager@gmail.com"))
                .thenReturn(Optional.of(currentUser));

        currentUser.setId(10L);
        currentUser.setEmail("manager@gmail.com");
        nextUser.setId(3L);
        task.setAssignedToUser(currentUser);
        currentStep.setStepOrder(1);
        nextStep.setStepOrder(2);
        processInstance.setStatus("RUNNING");
        task.setProcessInstance(processInstance);
        currentStep.setProcessDefinition(processDefinition);
        nextStep.setProcessDefinition(processDefinition);
        task.setProcessStep(currentStep);
        processDefinition.setId(1L);
        task.setStatus("PENDING");
        currentStep.setAssignedRole(managerRole);
        nextStep.setAssignedRole(financeRole);
        currentUser.setRole(managerRole);
        nextUser.setRole(financeRole);
        processDefinition.setName("Leave Application");
        processInstance.setProcessDefinition(processDefinition);

        when(taskRepository.findByIdForUpdate(21L)).thenReturn(Optional.of(task));

        when(processStepRepository.
                findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinition.getId(), currentStep.getStepOrder()))
                .thenReturn(Optional.of(nextStep));
        when(userRepository.findAllByRoleAndIsDeletedIsFalse
                (nextStep.getAssignedRole())).thenReturn(List.of(nextUser));

        when(taskRepository.countByAssignedToUserIdAndStatus(nextUser.getId(),"PENDING"))
                .thenReturn(2L);

        taskService.approveOrReject(21L, "manager@gmail.com", TaskActionType.APPROVE, null);

        assertEquals("APPROVED", task.getStatus());
        assertEquals("RUNNING", processInstance.getStatus());
        verify(taskRepository, times(2)).save(taskCaptor.capture());

        Task newTask = taskCaptor.getAllValues().get(1);
        assertEquals("PENDING", newTask.getStatus());
        assertEquals(nextUser.getId(), newTask.getAssignedToUser().getId());

        ArgumentCaptor<AuditLogCreateDTO> auditCaptor = ArgumentCaptor.forClass(AuditLogCreateDTO.class);

        verify(auditLogService).createAuditLog(auditCaptor.capture());

        AuditLogCreateDTO captureAudit = auditCaptor.getValue();

        assertEquals(task.getId(),captureAudit.getEntityId());
        assertEquals("TASK", captureAudit.getEntityType());
        assertEquals("TASK_APPROVED", captureAudit.getAction());
    }

    @Test
    void throwExceptionWhenNoEligibleUserFound(){

        User currentUser = new User();
        ProcessDefinition processDefinition = new ProcessDefinition();
        ProcessStep currentStep = new ProcessStep();
        ProcessStep nextStep = new ProcessStep();
        ProcessInstance processInstance = new ProcessInstance();
        Task task = new Task();
        Role managerRole = new Role();
        Role financeRole = new Role();

        currentUser.setId(10L);
        currentUser.setEmail("manager@gmail.com");
        processDefinition.setId(1L);
        currentStep.setStepOrder(1);
        nextStep.setStepOrder(2);

        processInstance.setStatus("RUNNING");
        task.setStatus("PENDING");

        task.setProcessInstance(processInstance);
        task.setProcessStep(currentStep);
        currentUser.setRole(managerRole);
        processInstance.setProcessDefinition(processDefinition);
        currentStep.setProcessDefinition(processDefinition);
        nextStep.setProcessDefinition(processDefinition);

        currentStep.setAssignedRole(managerRole);
        nextStep.setAssignedRole(financeRole);

        task.setAssignedToUser(currentUser);

        when(userRepository.findByEmailAndIsDeletedFalse("manager@gmail.com"))
                .thenReturn(Optional.of(currentUser));

        when(taskRepository.findByIdForUpdate(21L)).thenReturn(Optional.of(task));

        when(processStepRepository.
                findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinition.getId(), currentStep.getStepOrder()))
                .thenReturn(Optional.of(nextStep));
        when(userRepository.findAllByRoleAndIsDeletedIsFalse(nextStep.getAssignedRole()))
                .thenReturn(List.of());

        assertThrows(NoEligibleUserIsFoundException.class, ()-> taskService
                .approveOrReject(21L, "manager@gmail.com", TaskActionType.APPROVE, null));
        assertEquals("PENDING", task.getStatus());
        assertEquals("RUNNING", processInstance.getStatus());
    }
}

