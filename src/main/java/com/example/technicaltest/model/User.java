package com.example.technicaltest.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String username;

    private Date birthdate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    private String phoneNumber;

    public User() {
        this.username = "";
        this.birthdate = new Date();
        this.country =  new Country();
        this.gender = new Gender();
        this.phoneNumber = "";
    }

    public User(String username) {
        this.username = username;
        this.birthdate = new Date();
        this.country =  new Country();
        this.gender = new Gender();
        this.phoneNumber = "";
    }
}
