package com.suraj.bpms.project.dto.user;

import java.sql.Timestamp;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private RoleResponseDTO role;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id,
                           String name,
                           String email,
                           RoleResponseDTO role,
                           Timestamp createdAt,
                           Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleResponseDTO getRole() {
        return role;
    }

    public void setRole(RoleResponseDTO role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

}
