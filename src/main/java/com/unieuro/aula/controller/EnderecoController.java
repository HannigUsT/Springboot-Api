package com.unieuro.aula.controller;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import com.unieuro.aula.model.EnderecoEntity;
import com.unieuro.aula.repository.EnderecoRepository;
import com.unieuro.aula.response.ApiResponse;
import com.unieuro.aula.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class EnderecoController {
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/endereco/{cep}")
    public ResponseEntity<?> consultarEndereco(@PathVariable String cep, HttpServletRequest req) {
        String token = jwtTokenProvider.getTokenFromRequest(req);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = "https://viacep.com.br/ws/" + cep + "/json/";
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                return response;
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("error", "CEP não encontrado!"));
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    @GetMapping("/endereco/{cep}/{tipo}")
    public ResponseEntity<?> consultarTipo(@PathVariable String cep, @PathVariable Integer tipo,
            HttpServletRequest req) {
        String token = jwtTokenProvider.getTokenFromRequest(req);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            try {
                EnderecoEntity existingEndereco = enderecoRepository.findByCep(cep)
                        .orElseThrow(() -> new RuntimeException("Valor do Cep não existe!"));
                if (tipo == 1) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("success", "Bairro: " + existingEndereco.getBairro());
                    return ResponseEntity.status(HttpStatus.OK).body(errorMap);
                } else if (tipo == 2) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("success", "Cidade: " + existingEndereco.getLocalidade());
                    return ResponseEntity.status(HttpStatus.OK).body(errorMap);
                } else if (tipo == 3) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("success", "Logradouro: " + existingEndereco.getLogradouro());
                    return ResponseEntity.status(HttpStatus.OK).body(errorMap);
                } else if (tipo == 4) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("success", "UF: " + existingEndereco.getUf());
                    return ResponseEntity.status(HttpStatus.OK).body(errorMap);
                } else if (tipo == 5) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("success", "Codigo IBGE: " + existingEndereco.getIbge());
                    return ResponseEntity.status(HttpStatus.OK).body(errorMap);
                } else if (tipo == 6) {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("success", "Codigo SIAFI: " + existingEndereco.getSiafi());
                    return ResponseEntity.status(HttpStatus.OK).body(errorMap);
                } else {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("error", "Digite um tipo de 1 - 6");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
                }
            } catch (Exception e) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error",
                        "Favor verificar os valores de entrada, o CEP tem que estar neste formato XXXXX-XXX");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    @PostMapping("/endereco/{cep}")
    public ResponseEntity<?> salvarEndereco(@PathVariable String cep, HttpServletRequest req) {
        String token = jwtTokenProvider.getTokenFromRequest(req);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = "https://viacep.com.br/ws/" + cep + "/json/";
                ResponseEntity<EnderecoEntity> response = restTemplate.getForEntity(url, EnderecoEntity.class);
                EnderecoEntity endereco = response.getBody();
                enderecoRepository.save(endereco);
                return ResponseEntity.ok(endereco);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("error", "CEP não encontrado!"));
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

}
