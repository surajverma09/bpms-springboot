package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.EmailOtp;
import com.suraj.bpms.project.repository.EmailOtpRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Locale;


@Service
public class EmailOtpService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final String hmacSecret;
    private final EmailOtpRepository emailOtpRepository;
    @Value("${otp.expiration-minutes}")
    private long otpExpirationMinutes;

    public EmailOtpService(@Value("${otp.hmac-secret}") String hmacSecret,
                           EmailOtpRepository emailOtpRepository){
        this.hmacSecret = hmacSecret;
        this.emailOtpRepository = emailOtpRepository;
    }

    public String generateOtp(){
        int otp = secureRandom.nextInt(1_000_000);
            return String.format("%06d",otp);
    }

    public String hashOtp(String email, String otp) {
        try {
                Mac mac = Mac.getInstance("HmacSHA256");

                SecretKeySpec key = new SecretKeySpec
                        (hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

                mac.init(key);

                String value = email.trim().toLowerCase(Locale.ROOT) + ":" + otp;

                byte[] hash = mac.doFinal(
                        value.getBytes(StandardCharsets.UTF_8)
                );

                return HexFormat.of().formatHex(hash);
            }

        catch (Exception ex){
            throw  new IllegalStateException("Unable to hash OTP", ex);
        }
    }

    public String createAndSaveOtp(String email){

        email = email.trim().toLowerCase(Locale.ROOT);

        String otp = generateOtp();

        String otpHash = hashOtp(email, otp);

        EmailOtp emailOtp = emailOtpRepository.findByEmail(email)
                .orElseGet(EmailOtp::new);

        emailOtp.setEmail(email);
        emailOtp.setOtpHash(otpHash);
        emailOtp.setAttemptCount(0);
        emailOtp.setExpiresAt(LocalDateTime.now().plusMinutes(otpExpirationMinutes));

        emailOtpRepository.save(emailOtp);

        return otp; //returns OTP
    }

    public void invalidateOtp(String email){
        emailOtpRepository.deleteByEmail(email);
    }
}
