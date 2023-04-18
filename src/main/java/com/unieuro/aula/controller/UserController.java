package com.unieuro.aula.controller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unieuro.aula.model.UserEntity;
import com.unieuro.aula.repository.UserRepository;
import com.unieuro.aula.response.ApiResponse;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/usuarios")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user) {

        if (user.getName() == null || user.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Nome não pode ser vazio."));
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Senha não pode ser vazia."));
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Email não pode ser vazio."));
        }

        Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("error", "Usuário já está registrado"));
        }

        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        if (user.getEmail() == null || !user.getEmail().matches(emailPattern)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Erro ao criar o usuário, e-mail inválido."));
        }

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if (user.getPassword() == null || !user.getPassword().matches(passwordPattern)) {
            if (user.getPassword() == null || user.getPassword().length() < 8) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error",
                        "Erro ao criar o usuário, senha deve conter no mínimo 8 caracteres."));
            }
            if (!user.getPassword().matches(".*[A-Z].*")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error",
                        "Erro ao criar o usuário, senha deve conter ao menos uma letra maiúscula."));
            }
            if (!user.getPassword().matches(".*[a-z].*")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error",
                        "Erro ao criar o usuário, senha deve conter ao menos uma letra minúscula."));
            }
            if (!user.getPassword().matches(".*[0-9].*")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error",
                        "Erro ao criar o usuário, senha deve conter ao menos um número."));
            }
            if (!user.getPassword().matches(".*[@#$%^&+=].*")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error",
                        "Erro ao criar o usuário, senha deve conter ao menos um caractere especial."));
            }
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Collections.singletonMap("message", "Usuário criado com sucesso"));
    }
}
