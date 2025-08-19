package com.foorcourt.infraestructure.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;
    
    private static final String[] WHITE_LIST_OPENAPI = {"/swagger", "/swagger/**", "/api-docs", "/api-docs/**", "/webjars/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(WHITE_LIST_OPENAPI).permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/restaurants").permitAll()
                .requestMatchers("/api/v1/restaurants/**").hasRole("admin")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationWebFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}