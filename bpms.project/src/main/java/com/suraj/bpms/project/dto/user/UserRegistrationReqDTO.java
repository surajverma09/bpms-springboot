package com.suraj.bpms.project.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationReqDTO {

    @NotBlank(message = "Full Name should not be blank")
    private String fullName;

    @Size(max = 150)
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please use valid email")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain uppercase, lowercase, digit, and special character"
    )
    private String password;
}

