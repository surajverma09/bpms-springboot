package com.suraj.bpms.project.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDTO {

    @Email
    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    private String password;
}
