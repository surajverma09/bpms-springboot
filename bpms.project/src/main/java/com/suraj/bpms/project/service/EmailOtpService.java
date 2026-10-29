package com.suraj.bpms.project.service;

import com.suraj.bpms.project.entity.EmailOtp;
import com.suraj.bpms.project.exception.InvalidOtpException;
import com.suraj.bpms.project.exception.OtpAttemptLimitException;
import com.suraj.bpms.project.exception.OtpCooldownException;
import com.suraj.bpms.project.exception.OtpSendLimitException;
import com.suraj.bpms.project.repository.EmailOtpRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Optional;


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
            return String.format(Locale.ROOT,"%06d",otp);
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

        LocalDateTime now = LocalDateTime.now();

        Optional<EmailOtp> checkingEmail = emailOtpRepository.findByEmail(email);

        EmailOtp emailOtp = checkingEmail
                .orElseGet(EmailOtp::new);

        if(checkingEmail.isPresent() && emailOtp.getLastSendAt() != null && now
                .isBefore(emailOtp.getLastSendAt().plusSeconds(60))){

            throw new OtpCooldownException(
                    "Please wait before requesting another OTP"
            );
        }

        if(emailOtp.getCreatedAt() == null){
            emailOtp.setCreatedAt(now);
            emailOtp.setSendCount(1);

        }
        else if ((now.isEqual(emailOtp.getCreatedAt().plusMinutes(15))
                || now.isAfter(emailOtp.getCreatedAt().plusMinutes(15)))) {

            emailOtp.setSendCount(1);
            emailOtp.setCreatedAt(LocalDateTime.now());
        }
        else{
            if(emailOtp.getSendCount() >= 3){
                throw new OtpSendLimitException("OTP request limit reached. Please try again later ");
            }else{
                emailOtp.setSendCount(emailOtp.getSendCount() + 1);
            }
        }

        String otp = generateOtp();

        String otpHash = hashOtp(email, otp);

        emailOtp.setEmail(email);
        emailOtp.setOtpHash(otpHash);
        emailOtp.setAttemptCount(0);
        emailOtp.setExpiresAt(now.plusMinutes(otpExpirationMinutes));
        emailOtp.setLastSendAt(now);

        emailOtpRepository.save(emailOtp);


        return otp; //returns OTP
    }

    public void invalidateOtp(String email){

        email = email.trim().toLowerCase(Locale.ROOT);

        LocalDateTime now = LocalDateTime.now();

        emailOtpRepository.findByEmail(email).ifPresent(
                otpRecord -> {otpRecord.setExpiresAt(
                        now.minusMinutes(1));

                    emailOtpRepository.save(otpRecord);
        }
        );
    }

    public void otpVerification(String email, String submittedOtp){
        email = email.trim().toLowerCase(Locale.ROOT);

        Optional<EmailOtp> findRecord = emailOtpRepository.findByEmail(email);

        EmailOtp emailOtp = findRecord.orElseThrow(()->
                new IllegalArgumentException("No OTP request found for this email"));

        if(!LocalDateTime.now().isBefore(emailOtp.getExpiresAt())){
            throw new IllegalStateException("OTP has expired");
        }

        if(emailOtp.getAttemptCount() >= 5){
            throw new OtpAttemptLimitException("Too many incorrect attempts, please request a new OTP");
        }

        String submittedHash = hashOtp(email, submittedOtp);

        String entityHash = emailOtp.getOtpHash();

        byte[] submittedHashBytes = submittedHash.getBytes(StandardCharsets.UTF_8);
        byte[] entityHashBytes = entityHash.getBytes(StandardCharsets.UTF_8);

        boolean isHashEqual = MessageDigest.isEqual(submittedHashBytes, entityHashBytes);

        if(!isHashEqual){
            emailOtp.setAttemptCount(emailOtp.getAttemptCount() + 1);
            emailOtpRepository.save(emailOtp);

            throw new InvalidOtpException("Invalid otp entered");
        }

        emailOtp.setExpiresAt(LocalDateTime.now().minusMinutes(1));
        emailOtpRepository.save(emailOtp);
    }
}
