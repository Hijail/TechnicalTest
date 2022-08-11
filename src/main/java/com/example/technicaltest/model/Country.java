package com.example.technicaltest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "gender")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String country;

    public Country() {
        this.country = "";
    }

    public Country(String country) {
        this.country = country;
    }
}
