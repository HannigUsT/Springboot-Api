package com.unieuro.aula.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import com.unieuro.aula.model.SomaEntity;
import com.unieuro.aula.repository.SomaRepository;
import com.unieuro.aula.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class SomaController {

    @Autowired
    private SomaRepository somaRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/soma/{id}")
    public SomaEntity somaGet(@PathVariable long id, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        if (jwtTokenProvider.validateToken(token)) {
            return somaRepository.findById(id).orElse(null);
        } else {
            throw new AccessDeniedException("Token inválido");
        }
    }

    @PostMapping("/soma/{num1}/{num2}")
    public int somaPost(@PathVariable int num1, @PathVariable int num2, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        if (jwtTokenProvider.validateToken(token)) {
            int resultado = num1 + num2;
            SomaEntity somaEntity = new SomaEntity(num1, num2, resultado);
            somaRepository.save(somaEntity);
            return resultado;
        } else {
            throw new AccessDeniedException("Token inválido");
        }
    }

}