package com.suraj.bpms.project.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "process_instance_id", nullable = false)
    private ProcessInstance processInstance;

    @ManyToOne
    @JoinColumn(name = "process_step_id", nullable = false)
    private ProcessStep processStep;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedToUser;

    @Column(length = 30, nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "completed_at")
    private Timestamp completedAt;


    // Parameterized constructor
    public Task(Long id,
                ProcessInstance processInstance,
                ProcessStep processStep,
                User assignedToUser,
                String status,
                Timestamp createdAt,
                Timestamp completedAt) {

        this.id = id;
        this.processInstance = processInstance;
        this.processStep = processStep;
        this.assignedToUser = assignedToUser;
        this.status = status;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
    }


    // No-argument constructor
    public Task() {
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public ProcessStep getProcessStep() {
        return processStep;
    }

    public void setProcessStep(ProcessStep processStep) {
        this.processStep = processStep;
    }

    public User getAssignedToUser() {
        return assignedToUser;
    }

    public void setAssignedToUser(User assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Timestamp completedAt) {
        this.completedAt = completedAt;
    }
}