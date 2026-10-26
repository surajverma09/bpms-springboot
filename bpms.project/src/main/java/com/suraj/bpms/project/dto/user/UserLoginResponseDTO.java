package com.suraj.bpms.project.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String accessToken;

    private String tokenType;

    private Long expiresIn;

    private String email;
}
