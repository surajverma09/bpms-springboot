package com.suraj.bpms.project.dto.user;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

}