package com.example.technicaltest.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "gender")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String gender;

    public Gender() {
        this.gender = "";
    }

    public Gender(String gender) {
        this.gender = gender;
    }
}
