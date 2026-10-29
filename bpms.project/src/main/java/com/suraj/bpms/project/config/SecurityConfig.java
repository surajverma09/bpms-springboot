package com.suraj.bpms.project.config;

import com.suraj.bpms.project.security.JwtAuthenticationFilter;
import com.suraj.bpms.project.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authenticationProvider,
                                                   JwtAuthenticationFilter jwtFilter)
            throws Exception{
        http.csrf(
                csrf -> csrf.disable())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authenticationProvider(authenticationProvider)

                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"message\":\"Unauthorized\"}"
                            );
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write(
                                    "{\"message\":\"Forbidden\"}"
                            );
                        })
                )


                .authorizeHttpRequests(auth ->auth.requestMatchers
                        ("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/registration",
                                "/api/registration/verify-otp").permitAll()

                        .requestMatchers("/api/users", "/api/users/**").hasRole("ADMIN")

                        .requestMatchers("/api/roles", "/api/roles/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/process-definition",
                                "/api/process-definition/**").hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.GET, "/api/process-definition",
                                "/api/process-definition/**").hasAnyRole("ADMIN", "MANAGER", "HR", "FINANCE")

                        .requestMatchers(HttpMethod.POST, "/api/process-instance",
                                "/api/process-instance/**" ).hasAnyRole("ADMIN", "MANAGER", "HR", "FINANCE","EMPLOYEE")

                        .requestMatchers(HttpMethod.GET, "/api/process-instance",
                                "/api/process-instance/**").hasAnyRole("ADMIN", "MANAGER", "HR", "FINANCE", "EMPLOYEE")

                        .requestMatchers(HttpMethod.GET, "/api/process-step",
                                "/api/process-step/**").hasAnyRole("ADMIN", "MANAGER", "HR", "FINANCE")

                        .requestMatchers(HttpMethod.POST, "/api/process-step",
                                "/api/process-step/**").hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers("/api/audit-log", "/api/audit-log/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/users" , "/api/users/**")
                        .hasRole("ADMIN")


                        .anyRequest().authenticated());

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(
                                                            CustomUserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
