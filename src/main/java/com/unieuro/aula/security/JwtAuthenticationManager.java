package com.unieuro.aula.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationManager {

    public Boolean authenticate(String rawPassword, String encodedPassword) {

        if (!new BCryptPasswordEncoder().matches(rawPassword, encodedPassword)) {
            return false;
        }

        return true;
    }

}
