package com.example.technicaltest.model;

import javax.persistence.*;

import com.example.technicaltest.mapper.StringCountryConverter;
import com.example.technicaltest.mapper.StringGenderConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate birthdate;

    @ApiModelProperty(position = 3, required = true, value = "example: France")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    @Convert(converter = StringCountryConverter.class)
    private Country country;

    @ApiModelProperty(position = 4, value = "example: male / female / other")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "gender_id", referencedColumnName = "id")
    @Convert(converter = StringGenderConverter.class)
    private Gender gender;

    @ApiModelProperty(position = 5, value = "example: 01 23 45 67 89")
    private String phoneNumber;

    /**
     * Create User Object that can be saved in database
     *
     * @param username User name
     * @param birthdate User birthdate
     * @param country User country
     */
    public User(String username, LocalDate birthdate, Country country) {
        this.name = username;
        this.birthdate = birthdate;
        this.country =  country;
        this.gender = null;
        this.phoneNumber = null;
    }

    /**
     * define country setter
     * @param country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * define gender setter
     * @param gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
