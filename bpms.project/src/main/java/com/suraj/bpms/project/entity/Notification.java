package com.suraj.bpms.project.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "process_instance_id", nullable = false)
    private ProcessInstance processInstance;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(length = 500, nullable = false)
    private String message;

    @Column(name = "is_read", nullable = false )
    private Boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private Timestamp createdAt;


    //Constructor
    public Notification(Long id, User user,
                        ProcessInstance processInstance,
                        Task task,
                        String message,
                        Boolean isRead,
                        Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.processInstance = processInstance;
        this.task = task;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Notification() {
    }

    //Getter and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
