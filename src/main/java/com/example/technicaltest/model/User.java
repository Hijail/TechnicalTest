package com.example.technicaltest.model;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(position = 1, required = true, value = "example: Jean")
    @Column(name="USERNAME", length=50, nullable=false, unique=true)
    private String username;

    @ApiModelProperty(position = 2, required = true, value = "example: 2000-08-14T23:18:14.337Z")
    @Column(name="BIRTHDATE", nullable=false, unique=false)
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @ApiModelProperty(position = 3, required = true, value = "example: France")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;

    @ApiModelProperty(position = 4, required = false, value = "example: male / female / other")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    @ApiModelProperty(position = 5, required = false, value = "example: 01 23 45 67 89")
    private String phoneNumber;

    public User() {
        this.username = "";
        this.birthdate = null;
        this.country =  null;
        this.gender = null;
        this.phoneNumber = null;
    }

    public User(String username) {
        this.username = username;
        this.birthdate = null;
        this.country =  null;
        this.gender = null;
        this.phoneNumber = null;
    }
}
