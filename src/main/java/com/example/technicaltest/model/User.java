package com.example.technicaltest.model;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(position = 1, required = true, value = "example: Jean")
    @Column(name="NAME", length=50, nullable=false)
    private String name;

    @ApiModelProperty(position = 2, required = true, value = "example: 2000-08-14T23:18:14.337Z")
    @Column(name="BIRTHDATE", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @ApiModelProperty(position = 3, required = true, value = "example: France")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;

    @ApiModelProperty(position = 4, value = "example: male / female / other")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    private Gender gender;

    @ApiModelProperty(position = 5, value = "example: 01 23 45 67 89")
    private String phoneNumber;

    public User() {
        this.name = "";
        this.birthdate = null;
        this.country =  null;
        this.gender = null;
        this.phoneNumber = null;
    }

    /**
     * Create User Object that can be saved in database
     *
     * @param username User name
     * @param birthdate User birthdate
     * @param country User country
     */
    public User(String username, Date birthdate, Country country) {
        this.name = username;
        this.birthdate = birthdate;
        this.country =  country;
        this.gender = null;
        this.phoneNumber = null;
    }
}
