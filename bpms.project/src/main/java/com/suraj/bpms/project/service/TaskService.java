package com.suraj.bpms.project.service;

import java.sql.Timestamp;
import com.suraj.bpms.project.dto.task.TaskResponseDTO;
import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.enums.TaskActionType;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.mapper.TaskMapper;
import com.suraj.bpms.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final ProcessStepRepository processStepRepository;
    private final ProcessInstanceRepository processInstanceRepository;


    public List<TaskResponseDTO> getAllTask(){
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toTaskResponseDTO)
                .toList();
    }


    public TaskResponseDTO getTaskById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task is not found with this id : " + id));

        return taskMapper.toTaskResponseDTO(task);
    }

    @Transactional
    public TaskResponseDTO approveOrReject(Long taskId, Long userId,
                                TaskActionType typeAction){
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task is not found with this id : " + taskId));

        System.out.println(
                "Task ID: " + task.getId()
                        + ", Status before action: " + task.getStatus()
                        + ", Action: " + typeAction
        );

        if(!task.getStatus().equals("PENDING")){
            throw new InvalidTaskStatusException("Task has already been processed. Current status: " + task.getStatus());

        }
        if(!task.getAssignedToUser().getId().equals(userId)){
            throw new UnauthorizedUserFoundException("User is not authorized to perform this task action");
        }
        if(typeAction == null){
            throw new InvalidTaskActionException("Task action can't be null");
        }

        if (typeAction == TaskActionType.REJECT) {
            task.setStatus("REJECTED");
            task.setCompletedAt(Timestamp.from(
                    java.time.Instant.now()));
            task.getProcessInstance().setStatus("REJECTED");

            taskRepository.save(task);

            return taskMapper.toTaskResponseDTO(task);
        }

        Integer currentStepOrder = task.getProcessStep().getStepOrder();
        Long processDefinitionId = task.getProcessStep().getProcessDefinition().getId();

        Optional<ProcessStep> nextProcessStep = processStepRepository
                .findFirstByProcessDefinitionIdAndStepOrderGreaterThanOrderByStepOrderAsc
                        (processDefinitionId,currentStepOrder);

        if (nextProcessStep.isEmpty()) {
            task.setStatus("APPROVED");
            System.out.println("Task status after approval: " + task.getStatus());

            task.setCompletedAt(Timestamp.from(
                    java.time.Instant.now()));
            task.getProcessInstance().setStatus("COMPLETED");

            taskRepository.save(task);

            return taskMapper.toTaskResponseDTO(task);
        }
        ProcessStep processStep = nextProcessStep.get();

        List<User> eligibleUsers = userRepository
                .findAllByRoleAndIsDeletedIsFalse(processStep.getAssignedRole());


        if(eligibleUsers.isEmpty()){
            throw new NoEligibleUserIsFoundException("No active eligible user is found for the next step");
        }

        User selectedUser = eligibleUsers.get(0);
        Long currentSmallestCount = taskRepository
                     .countByAssignedToUserIdAndStatus(selectedUser.getId(), "PENDING");

        for (int i = 1; i < eligibleUsers.size(); i++) {

            User currentUser = eligibleUsers.get(i);

            Long currentCount = taskRepository
                    .countByAssignedToUserIdAndStatus(currentUser.getId(), "PENDING");

            if(currentCount < currentSmallestCount || currentCount.equals(currentSmallestCount)
                        && currentUser.getId() < selectedUser.getId()){

                currentSmallestCount = currentCount;
                selectedUser = currentUser;
            }

        }
        Task newTask = new Task();

        newTask.setProcessInstance(task.getProcessInstance());
        newTask.setProcessStep(processStep);
        newTask.setAssignedToUser(selectedUser);
        newTask.setStatus("PENDING");
        task.setStatus("APPROVE");
        task.setCompletedAt(Timestamp.from(
                java.time.Instant.now()));


        taskRepository.save(task);
        taskRepository.save(newTask);

        return taskMapper.toTaskResponseDTO(task);
    }
}
