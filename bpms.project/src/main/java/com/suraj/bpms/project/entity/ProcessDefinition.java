package com.suraj.bpms.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "process_definitions")
public class ProcessDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    public ProcessDefinition(Long id, String name, String description, Integer version, User createdBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.createdBy = createdBy;
    }
    public ProcessDefinition() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
