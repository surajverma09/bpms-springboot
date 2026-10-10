package com.suraj.bpms.project.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "notifications")
public class Notification {

    //Getter and Setter
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

}
