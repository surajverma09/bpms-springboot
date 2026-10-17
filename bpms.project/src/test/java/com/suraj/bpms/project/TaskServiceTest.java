package com.suraj.bpms.project;

import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.mapper.TaskMapper;
import com.suraj.bpms.project.repository.ProcessStepRepository;
import com.suraj.bpms.project.repository.TaskRepository;
import com.suraj.bpms.project.repository.UserRepository;
import com.suraj.bpms.project.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    @Mock
    UserRepository userRepository;
    @Mock
    ProcessStepRepository processStepRepository;



    @InjectMocks
    TaskService taskService;


    @Test //test -> 1
    void shouldThrowAnExceptionWhenTaskIsAlreadyApproved(){

        Task task = new Task();

        task.setStatus("APPROVED");

        when(taskRepository.findById(21L)).
                thenReturn(Optional.of(task));


        assertThrows(
                InvalidTaskStatusException.class,
                ()->taskService.approveOrReject(
                        21L,10l, TaskActionType.APPROVE
                )
        );
    }
    @Test //test -> 2
    void shouldThrowExceptionWhenUserIsNotAuthorisedOrAssignedToTask(){

        Task task = new Task();
        User user = new User();

        task.setStatus("PENDING");
        user.setId(10L);
        task.setAssignedToUser(user);

        when(taskRepository.findById(21L)).thenReturn(Optional.of(task));

        assertThrows(UnauthorizedUserFoundException.class,
                ()->taskService.approveOrReject
                        (21L, 20L, TaskActionType.APPROVE));
    }

    @Test //test -> 3
    void shouldThrowExceptionWhenTaskIsNotFound(){

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,()-> taskService.approveOrReject
                (99L, 10L, TaskActionType.APPROVE));
    }

    @Test //test -> 4
    void shouldThrowExceptionWhenTaskActionIsNotFound(){

        Task task = new Task();
        User user = new User();
        task.setStatus("PENDING");
        user.setId(10L);
        task.setAssignedToUser(user);

        when(taskRepository.findById(21L)).thenReturn(Optional.of(task));

        assertThrows(InvalidTaskActionException.class,()->
                taskService.approveOrReject(21L, 10L, null));
    }

    @Test //test -> 5
    void shouldRejectProcessInstanceWhenUserRejectTask(){

        ProcessInstance processInstance = new ProcessInstance();
        Task task = new Task();
        User user = new User();

        processInstance.setStatus("RUNNING");
        task.setStatus("PENDING");

        user.setId(10L);
        task.setProcessInstance(processInstance);
        task.setAssignedToUser(user);

        when(taskRepository.findById(21L)).thenReturn(Optional.of(task));


        taskService.approveOrReject(21L, 10L, TaskActionType.REJECT);

        assertEquals("REJECTED", task.getStatus());
        assertEquals("REJECTED", processInstance.getStatus());
        assertNotNull(task.getCompletedAt());

    }

    @Test
    void shouldCompleteProcessInstanceWhenDirectorApprovesIt(){
        User user = new User();
        ProcessInstance processInstance = new ProcessInstance();
        ProcessDefinition processDefinition = new ProcessDefinition();
        ProcessStep processStep = new ProcessStep();
        Task task = new Task();

        processStep.setProcessDefinition(processDefinition);
        processStep.setStepOrder(3);
        processInstance.setStatus("RUNNING");
        task.setStatus("PENDING");
        task.setProcessInstance(processInstance);
        task.setProcessStep(processStep);
        user.setId(10L);
        task.setAssignedToUser(user);

        when(taskRepository.findById(21L)).thenReturn(Optional.of(task));
        when(processStepRepository
                .findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinition.getId(),processStep.getStepOrder()))
                .thenReturn(Optional.empty());

        taskService.approveOrReject(21L, 10L, TaskActionType.APPROVE);

        assertEquals("APPROVED", task.getStatus());
        assertEquals("COMPLETED", processInstance.getStatus());
        assertNotNull(task.getCompletedAt());

        verify(taskRepository, times(1)).save(task);

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

        currentUser.setId(10L);
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

        when(taskRepository.findById(21L)).thenReturn(Optional.of(task));

        when(processStepRepository.
                findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinition.getId(), currentStep.getStepOrder()))
                .thenReturn(Optional.of(nextStep));
        when(userRepository.findAllByRoleAndIsDeletedIsFalse
                (nextStep.getAssignedRole())).thenReturn(List.of(nextUser));

        when(taskRepository.countByAssignedToUserIdAndStatus(nextUser.getId(),"PENDING"))
                .thenReturn(2L);

        taskService.approveOrReject(21L, 10L, TaskActionType.APPROVE);

        assertEquals("APPROVED", task.getStatus());
        assertEquals("RUNNING", processInstance.getStatus());
        verify(taskRepository, times(2)).save(taskCaptor.capture());

        Task newTask = taskCaptor.getAllValues().get(1);
        assertEquals("PENDING", newTask.getStatus());
        assertEquals(nextUser.getId(), newTask.getAssignedToUser().getId());
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

        when(taskRepository.findById(21L)).thenReturn(Optional.of(task));
        when(processStepRepository.
                findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinition.getId(), currentStep.getStepOrder()))
                .thenReturn(Optional.of(nextStep));
        when(userRepository.findAllByRoleAndIsDeletedIsFalse(nextStep.getAssignedRole()))
                .thenReturn(List.of());

        assertThrows(NoEligibleUserIsFoundException.class, ()-> taskService
                .approveOrReject(21L, 10L, TaskActionType.APPROVE));
        assertEquals("PENDING", task.getStatus());
        assertEquals("RUNNING", processInstance.getStatus());
    }
}

