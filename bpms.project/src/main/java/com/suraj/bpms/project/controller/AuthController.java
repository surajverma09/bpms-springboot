package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.user.UserLoginRequestDTO;
import com.suraj.bpms.project.dto.user.UserLoginResponseDTO;
import com.suraj.bpms.project.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login")
    public UserLoginResponseDTO loginAuth(
            @Valid @RequestBody UserLoginRequestDTO loginReq){
        
        return authService.authenticate(
                loginReq.getEmail(),
                loginReq.getPassword()
        );
    }
}