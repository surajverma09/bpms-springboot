package com.suraj.bpms.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
}
