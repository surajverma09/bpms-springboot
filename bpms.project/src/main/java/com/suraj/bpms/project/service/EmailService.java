package com.suraj.bpms.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String email,String otp) {

        String registrationMessage =
                "Hello,\n" +
                "\n" +
                "Thank you for registering with our platform.\n" +
                "\n" +
                "To complete your registration, please use the verification code below to verify your email address.\n" +
                "YOUR VERIFICATION CODE : " +
                otp + "\n" +
                "Valid for 3 minutes\n" +
                "\n" +
                "Please enter this code on the registration verification page to activate your account.\n" +
                "\n" +
                "For your security, do not share this code with anyone.\n" +
                "\n" +
                "If you did not initiate this registration, you can safely ignore this email.\n" +
                "\n" +
                "Regards,\n" +
                "BPMS Team";

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Verify Your Email - User Registration");
        simpleMailMessage.setText(registrationMessage);

        javaMailSender.send(simpleMailMessage);
    }
}
