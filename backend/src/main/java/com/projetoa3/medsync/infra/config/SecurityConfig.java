package com.projetoa3.medsync.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable for production)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register").permitAll() // Allow registration without auth
                        .anyRequest().authenticated() // All other requests need authentication
                );

        return http.build();
    }
}
