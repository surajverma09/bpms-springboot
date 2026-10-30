package com.suraj.bpms.project;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestMailConfiguration {

    @Bean
    @Primary
    public JavaMailSender testJavaMailSender(){
        return Mockito.mock(JavaMailSender.class);
    }
}
    