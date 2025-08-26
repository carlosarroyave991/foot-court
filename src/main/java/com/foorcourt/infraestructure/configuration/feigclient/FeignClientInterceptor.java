package com.foorcourt.infraestructure.configuration.feigclient;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Extrae automaticamente el token del contexto de seguridad
 */
@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();
            // El token ya viene con "Bearer " desde JwtUtil.getAuthentication()
            if (token.startsWith("Bearer ")) {
                template.header("Authorization", token);
            } else {
                template.header("Authorization", "Bearer " + token);
            }
        }
    }
}