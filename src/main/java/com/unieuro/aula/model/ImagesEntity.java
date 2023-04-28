package com.unieuro.aula.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "images")
public class ImagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "date")
    private Timestamp date;

    @Column(name = "image_path")
    private String image_path;

    public ImagesEntity() {
    }

    public ImagesEntity(Long user_id) {
        this.user_id = user_id;
        this.date = Timestamp.from(Instant.now());
    }

}
