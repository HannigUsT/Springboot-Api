package com.unieuro.aula.model;

import jakarta.persistence.*;

@Entity
@Table(name = "calculator")
public class CalculatorEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "result")
    private float result;

    public CalculatorEntity() {
    }

    public CalculatorEntity(Long id) {
        this.id = id;
        this.result = 0;
    }

    public CalculatorEntity(float result) {
        this.result = result;
    }

    public CalculatorEntity(Long id, float result) {
        this.id = id;
        this.result = result;
    }

    public float getResult() {
        return result;
    }

}
