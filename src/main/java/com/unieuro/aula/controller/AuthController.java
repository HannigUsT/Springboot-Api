package com.unieuro.aula.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unieuro.aula.model.UserEntity;
import com.unieuro.aula.repository.UserRepository;
import com.unieuro.aula.response.ApiResponse;
import com.unieuro.aula.security.JwtAuthenticationManager;
import com.unieuro.aula.security.JwtTokenProvider;

@RestController
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtAuthenticationManager jwtAuthenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody UserEntity user) {
        Optional<UserEntity> userExists = userRepository.findByEmail(user.getEmail());
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getPassword() == null
                || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Email ou senha não podem estar em branco."));
        }

        if (!userExists.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Senha ou Email invalido."));
        }

        if (!jwtAuthenticationManager.authenticate(user.getPassword(),
                userExists.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Senha ou Email invalido."));
        }
        String jwt = jwtTokenProvider.generateToken(userExists);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("success", jwt));
    }
}
