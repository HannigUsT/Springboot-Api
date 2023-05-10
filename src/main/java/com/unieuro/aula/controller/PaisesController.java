package com.unieuro.aula.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PathVariable;

import com.unieuro.aula.model.PaisesEntity;
import com.unieuro.aula.repository.PaisesRepository;

@RestController
public class PaisesController {
    @Autowired
    private PaisesRepository paisesRepository;

    @GetMapping("/paises/{languages}")
    public ResponseEntity<?> consultarPaises(@PathVariable String languages) {
        RestTemplate restTemplate = new RestTemplate();
        List<PaisesEntity> paises = new ArrayList<>();
        PaisesEntity[] response = restTemplate.getForObject("https://restcountries.com/v3.1/lang/" + languages,
                PaisesEntity[].class);
        System.out.println(response);
        for (PaisesEntity pais : response) {
            paises.add(pais);
        }

        paisesRepository.saveAll(paises);
        return ResponseEntity.ok(Collections.singletonMap("message", "Usuário deletado com sucesso!"));

    }
}
