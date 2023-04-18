package com.unieuro.aula.repository;

import com.unieuro.aula.model.SomaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SomaRepository extends JpaRepository<SomaEntity, Long> {
}