// Country.java
package com.unieuro.aula.model;

import jakarta.persistence.*;

@Entity
@Table(name = "paises")
public class PaisesEntity {
    
    public PaisesEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;
    
    @Column(name = "languages")
    private String languages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }
    
}