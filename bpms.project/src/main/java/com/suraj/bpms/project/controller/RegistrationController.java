package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.user.UserRegistrationReqDTO;
import com.suraj.bpms.project.dto.user.UserResponseDTO;
import com.suraj.bpms.project.dto.user.VerifyOtpRequestDTO;
import com.suraj.bpms.project.response.ApiResponse;
import com.suraj.bpms.project.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ApiResponse> creatingEmployeeUser(
            @Valid @RequestBody UserRegistrationReqDTO userReq
    ){
        registrationService.userRegistration(userReq);
        return ResponseEntity.ok(new ApiResponse("OTP sent successfully. Please check your email"));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO
            ){
        UserResponseDTO createdUser = registrationService.verifyAndCompleteReg
                (verifyOtpRequestDTO.getEmail(), verifyOtpRequestDTO.getOtp());

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
