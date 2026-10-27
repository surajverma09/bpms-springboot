package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.user.UserRegistrationReqDTO;
import com.suraj.bpms.project.entity.PendingRegistration;
import com.suraj.bpms.project.repository.PendingRegistrationRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final UserRepository userRepository;
    private final EmailOtpService emailOtpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public void userRegistration(UserRegistrationReqDTO userRegistrationReqDTO){


        String email = userRegistrationReqDTO.getEmail().trim().toLowerCase(Locale.ROOT);

        boolean userExist = userRepository.findByEmailAndIsDeletedFalse(email)
                .isPresent();

        if(userExist){
            throw new IllegalArgumentException("Email already exists");
        }

        String hashPassword = passwordEncoder.encode(userRegistrationReqDTO.getPassword());

        PendingRegistration pr = new PendingRegistration();
        LocalDateTime currentTime = LocalDateTime.now();

        pr.setEmail(email);
        pr.setPasswordHash(hashPassword);
        pr.setName(userRegistrationReqDTO.getFullName());
        pr.setExpireAt(currentTime.plusMinutes(15));
        pr.setCreatedAt(currentTime);

        pendingRegistrationRepository.save(pr);

        String otp = emailOtpService.createAndSaveOtp(email);

        try{
            emailService.sendOtpEmail(email, otp);
        }catch (Exception e){
            emailOtpService.invalidateOtp(email);
            throw new IllegalStateException ("Please try again later", e);
        }
    }
}
