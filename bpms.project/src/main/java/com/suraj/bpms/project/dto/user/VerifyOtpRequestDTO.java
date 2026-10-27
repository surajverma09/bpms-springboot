package com.suraj.bpms.project.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequestDTO {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "OTP must not be blank")
    @Pattern(
            regexp = "\\d{6}",
            message = "OTP must contain exactly 6 digits"
    )
    private String otp;
}
