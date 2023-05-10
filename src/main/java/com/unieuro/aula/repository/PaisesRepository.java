package com.unieuro.aula.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unieuro.aula.model.PaisesEntity;

@Repository
public interface PaisesRepository extends JpaRepository<PaisesEntity, Long> {
    
    List<PaisesEntity> findByLanguages(String languages);
    
}