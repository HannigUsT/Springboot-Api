package com.unieuro.aula.repository;

import com.unieuro.aula.model.CalculatorEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculatorRepository extends JpaRepository<CalculatorEntity, Long> {

}