package com.unieuro.aula.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.el.lang.FunctionMapperImpl.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import com.unieuro.aula.model.UserEntity;
import com.unieuro.aula.model.EnderecoEntity;
import com.unieuro.aula.repository.EnderecoRepository;
import com.unieuro.aula.repository.UserRepository;
import com.unieuro.aula.response.ApiResponse;


@RestController
public class EnderecoController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @GetMapping("/endereco/{cep}")
    public ResponseEntity<?> consultarEndereco(@PathVariable String cep){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://viacep.com.br/ws/"+cep+"/json/";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response;
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse("error", "CEP não encontrado!"));
        }
    }

    @GetMapping("/endereco")
    public List<EnderecoEntity> getAllUsers() {
        return enderecoRepository.findAll();
    }

    @GetMapping("/endereco/{cep}/{tipo}")
    public String consultarTipo(@PathVariable String cep, @PathVariable Integer tipo){
        try{
            EnderecoEntity existingEndereco = enderecoRepository.findByCep(cep).orElseThrow(() -> new RuntimeException("Valor do Cep não existe!"));
            
            System.out.println(existingEndereco.getBairro());

            if(tipo == 1){
                return "Bairro: "+existingEndereco.getBairro();
            }

            if(tipo == 2){
                return "Cidade: "+existingEndereco.getLocalidade();
            }

            if(tipo == 3){
                return "Logradouro: "+existingEndereco.getLogradouro();
            }
            
            if(tipo == 4){
                return "UF: "+existingEndereco.getUf();
            }

            if(tipo == 5){
                return "Codigo IBGE: "+existingEndereco.getIbge();
            }

            if(tipo == 6){
                return "Codigo SIAFI: "+existingEndereco.getSiafi();
            }

            return "Digite um tipo de 1 - 6";
        }catch(Exception e) {
            return "Favor verificar os valores de entrada, o CEP tem que estar neste formato XXXXX-XXX";
        }
    }

    
    @PostMapping("/endereco/{cep}")
    public ResponseEntity<?> salvarEndereco(@PathVariable String cep){
        try{
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://viacep.com.br/ws/"+cep+"/json/";
            ResponseEntity<EnderecoEntity> response = restTemplate.getForEntity(url, EnderecoEntity.class);
            EnderecoEntity endereco = response.getBody();
            enderecoRepository.save(endereco);
            return ResponseEntity.ok(endereco);
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse("error", "CEP não encontrado!"));
        }
    }

}
