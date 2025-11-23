package com.domaincrud.domaincrud.config;

import com.domaincrud.domaincrud.service.EmployeeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final EmployeeUserDetailsService employeeUserDetailsService;

    public SecurityConfig(EmployeeUserDetailsService employeeUserDetailsService) {
        this.employeeUserDetailsService = employeeUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Swagger public
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Sirf ADMIN (department_id = 1) ko domains ke saare APIs
                        .requestMatchers("/api/domains/**").hasRole("ADMIN")

                        // Baaki sab ko auth chahiye
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        // 👉 Spring Security ko bolo ki ye wala UserDetailsService use kare
        http.userDetailsService(employeeUserDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DB me "{noop}admin123" jaisa password hai to ye encoder sahi hai
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
