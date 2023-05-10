package com.unieuro.aula.repository;

import com.unieuro.aula.model.EnderecoEntity;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<EnderecoEntity, Long>{
    Optional<EnderecoEntity> findByCep(String cep);
}
