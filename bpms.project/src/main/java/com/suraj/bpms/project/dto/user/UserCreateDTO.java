package com.suraj.bpms.project.dto.user;


import jakarta.validation.constraints.*;

public class UserCreateDTO {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(
            message = "Password must contain uppercase, lowercase, number and special character",
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$*!?&]).+$"
    )
    private String password;

    @NotNull
    private Long roleId;

    public UserCreateDTO() {
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
