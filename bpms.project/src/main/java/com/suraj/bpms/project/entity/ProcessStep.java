package com.suraj.bpms.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "process_step")
public class ProcessStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "process_definition_id", nullable = false)
    private ProcessDefinition processDefinition;

    @ManyToOne
    @JoinColumn(name = "assigned_role_id", nullable = false)
    private Role assignedRole;

    @Column(length = 100, nullable = false)
    private String stepName;

    @Column(nullable = false)
    private Integer stepOrder;

    public ProcessStep(Long id, ProcessDefinition processDefinition, Role assignedRole, String stepName, Integer stepOrder) {
        this.id = id;
        this.processDefinition = processDefinition;
        this.assignedRole = assignedRole;
        this.stepName = stepName;
        this.stepOrder = stepOrder;
    }
    public ProcessStep(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public Role getAssignedRole() {
        return assignedRole;
    }

    public void setAssignedRole(Role assignedRole) {
        this.assignedRole = assignedRole;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }
}
