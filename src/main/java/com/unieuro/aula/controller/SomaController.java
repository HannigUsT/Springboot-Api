package com.unieuro.aula.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.unieuro.aula.model.SomaEntity;
import com.unieuro.aula.repository.SomaRepository;

@RestController
public class SomaController {

    @Autowired
    private SomaRepository somaRepository;

    @GetMapping("/soma/{id}")
    public SomaEntity somaGet(@PathVariable long id) {
        return somaRepository.findById(id).orElse(null);
    }

    @PostMapping("/soma/{num1}/{num2}")
    public int somaPost(@PathVariable int num1, @PathVariable int num2) {
        int resultado = num1 + num2;
        SomaEntity somaEntity = new SomaEntity(num1, num2, resultado);
        somaRepository.save(somaEntity);
        return resultado;
    }
}