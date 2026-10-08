package com.suraj.bpms.project.dto.user;

import jakarta.validation.constraints.*;

public class UserUpdateDTO  {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(
            message = "Password must contain uppercase, lowercase, number and special character",
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$*!?&]).+$"
    )
    private String password;

    @NotNull
    private Long roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}