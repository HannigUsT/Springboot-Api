package com.unieuro.aula.model;

import jakarta.persistence.*;

@Entity
@Table(name = "soma_resultados")
public class SomaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num1")
    private int num1;

    @Column(name = "num2")
    private int num2;

    @Column(name = "resultado")
    private int resultado;

    public SomaEntity() {
    }

    public SomaEntity(int num1, int num2, int resultado) {
        this.num1 = num1;
        this.num2 = num2;
        this.resultado = resultado;
    }

    public int getResultado() {
        return resultado;
    }

}
