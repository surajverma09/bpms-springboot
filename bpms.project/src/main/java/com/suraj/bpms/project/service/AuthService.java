package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.user.UserLoginResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public UserLoginResponseDTO authenticate(String email, String password){

        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(email, password);

        Authentication result = authenticationManager.authenticate(token);

        return new UserLoginResponseDTO(
                jwtService.generateToken(result.getName()),
                "Bearer",
                jwtService.getExpirationSeconds(),
                result.getName()
        );
    }
}
