package com.suraj.bpms.project.service;

import com.suraj.bpms.project.dto.user.UserRegistrationReqDTO;
import com.suraj.bpms.project.dto.user.UserResponseDTO;
import com.suraj.bpms.project.dto.role.RoleResponseDTO;
import com.suraj.bpms.project.entity.PendingRegistration;
import com.suraj.bpms.project.entity.Role;
import com.suraj.bpms.project.entity.User;
import com.suraj.bpms.project.exception.InvalidOtpException;
import com.suraj.bpms.project.mapper.UserMapper;
import com.suraj.bpms.project.repository.PendingRegistrationRepository;
import com.suraj.bpms.project.repository.RoleRepository;
import com.suraj.bpms.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final UserRepository userRepository;
    private final EmailOtpService emailOtpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public void userRegistration(UserRegistrationReqDTO userRegistrationReqDTO){


        String email = userRegistrationReqDTO.getEmail().trim().toLowerCase(Locale.ROOT);

        boolean userExist = userRepository.findByEmailAndIsDeletedFalse(email)
                .isPresent();

        if(userExist){
            throw new IllegalArgumentException("Email already exists");
        }

        String hashPassword = passwordEncoder.encode(userRegistrationReqDTO.getPassword());

        Optional<PendingRegistration> checkingUser = pendingRegistrationRepository.findByEmail(email);

        PendingRegistration pr =checkingUser
                .orElseGet(()-> new PendingRegistration());

        LocalDateTime currentTime = LocalDateTime.now();

        pr.setEmail(email);
        pr.setPasswordHash(hashPassword);
        pr.setName(userRegistrationReqDTO.getFullName());
        pr.setExpireAt(currentTime.plusMinutes(15));
        pr.setCreatedAt(currentTime);

        String otp = emailOtpService.createAndSaveOtp(email);

        try{
            pendingRegistrationRepository.save(pr);
            emailService.sendOtpEmail(email, otp);
        }catch (Exception e){
            emailOtpService.invalidateOtp(email);
            throw new IllegalStateException ("Please try again later", e);
        }
    }

    @Transactional(noRollbackFor = InvalidOtpException.class)
    public UserResponseDTO verifyAndCompleteReg(String email, String submittedOtp){

        email = email.trim().toLowerCase(Locale.ROOT);

        PendingRegistration pendingRegistration = pendingRegistrationRepository.findByEmail(email).orElseThrow(()->
                new IllegalArgumentException("No pending registration is found"));

        if(!LocalDateTime.now().isBefore(pendingRegistration.getExpireAt())){
            throw new IllegalStateException("Pending registration has expired, Please register again");
        }

        Role employeeRole = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(()->new IllegalStateException("Employee role doesn't exist"));

        emailOtpService.otpVerification(email, submittedOtp);

        User user = new User();

        user.setRole(employeeRole);
        user.setName(pendingRegistration.getName());
        user.setPassword(pendingRegistration.getPasswordHash());
        user.setEmail(pendingRegistration.getEmail());
        user.setIsDeleted(false);

        User savedUser = userRepository.save(user);

        pendingRegistrationRepository.deleteByEmail(email);

        return userMapper.toResponseDTO(savedUser);
    }
}
