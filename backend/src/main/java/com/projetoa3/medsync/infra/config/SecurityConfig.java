package com.projetoa3.medsync.infra.config;

import com.projetoa3.medsync.infra.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/users/register",
                                "/users/login"
                        ).permitAll()
                        .requestMatchers("/users/update").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
