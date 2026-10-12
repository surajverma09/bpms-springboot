package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.processinstance.ProcessInstanceCreateDTO;
import com.suraj.bpms.project.dto.processinstance.ProcessInstanceResponseDTO;
import com.suraj.bpms.project.entity.*;
import com.suraj.bpms.project.exception.*;
import com.suraj.bpms.project.mapper.ProcessInstanceMapper;
import com.suraj.bpms.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProcessInstanceService {

    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;
    private final UserRepository userRepository;
    private final ProcessStepRepository processStepRepository;
    private final ProcessInstanceMapper processInstanceMapper;
    private final TaskRepository taskRepository;


    public List<ProcessInstanceResponseDTO> getAllProcessInstances(){
        return processInstanceRepository.findAll()
                .stream()
                .map(processInstanceMapper::toProcessInstanceResponse)
                .toList();
    }



    public ProcessInstanceResponseDTO getProcessInstanceById(Long id){
        ProcessInstance processInstance =  processInstanceRepository.findById(id)
                .orElseThrow(() -> new ProcessInstanceNotFoundException
                        ("Process instance is not found with this id : " + id));

        return processInstanceMapper.toProcessInstanceResponse(processInstance);
    }


    @Transactional
    public ProcessInstanceResponseDTO createProcessInstance(Long processDefinitionId,
                                                            Long userId,
                                                            ProcessInstanceCreateDTO processInstanceCreateDTO){

        ProcessDefinition processDefinition = processDefinitionRepository.findById(processDefinitionId)
                .orElseThrow(()-> new ProcessDefinitionNotFoundException
                        ("Process Definition is not found with this id : "+ processDefinitionId));

        ProcessStep processDefinitionStep = processStepRepository
                .findFirstByProcessDefinitionIdOrderByStepOrderAsc(processDefinitionId)
                .orElseThrow(()-> new ProcessDefinitionHasNoStepException("Process definition has no step"));

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("User is not found with this id : " + userId));

//        Role role = processDefinitionStep.getAssignedRole();

        List<User> eligibleUsers = userRepository.findAllByRoleAndIsDeletedIsFalse(
                processDefinitionStep.getAssignedRole()
        );

        if(eligibleUsers.isEmpty()){
            throw new NoEligibleUserIsFoundException("No eligible user is found");
        }

        User selectedUser = eligibleUsers.get(0);
        Long smallestCount = taskRepository
                .countByAssignedToUserIdAndStatus(selectedUser.getId(), "PENDING");
        for (int i = 1; i < eligibleUsers.size(); i++) {

            User currentUser = eligibleUsers.get(i);

            Long currentCount = taskRepository
                    .countByAssignedToUserIdAndStatus(currentUser.getId(), "PENDING");

            if (currentCount < smallestCount ||
                    (currentCount.equals(smallestCount) &&
                            currentUser.getId() < selectedUser.getId())) {

                smallestCount = currentCount;
                selectedUser = currentUser;
            }
        }
        ProcessInstance processInstance = processInstanceMapper
                .toProcessInstanceEntity(processInstanceCreateDTO);

        processInstance.setProcessDefinition(processDefinition);
        processInstance.setStartedBy(user);
        processInstance.setStatus("RUNNING");

        Task task = new Task();

        ProcessInstance savedProcessInstance = processInstanceRepository.save(processInstance);

        task.setProcessInstance(savedProcessInstance);
        task.setProcessStep(processDefinitionStep);
        task.setAssignedToUser(selectedUser);
        task.setStatus("PENDING");
        taskRepository.save(task);
        return processInstanceMapper.toProcessInstanceResponse(savedProcessInstance);
    }
}
