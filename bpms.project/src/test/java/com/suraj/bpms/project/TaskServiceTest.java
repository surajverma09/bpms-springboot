package com.suraj.bpms.project;

import com.suraj.bpms.project.entity.ProcessInstance;
import com.suraj.bpms.project.entity.Task;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.mapper.TaskMapper;
import com.suraj.bpms.project.repository.ProcessStepRepository;
import com.suraj.bpms.project.repository.TaskRepository;
import com.suraj.bpms.project.repository.UserRepository;
import com.suraj.bpms.project.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

}

