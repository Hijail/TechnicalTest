package com.example.technicaltest.model;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Data
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(position = 1, required = true, value = "example: France")
    private String country;

    @ApiModelProperty(position = 2, required = true, value = "example: 18")
    private int legalAge;

    public Country() {
        this.legalAge = 0;
        this.country = "";
    }

    public Country(String country, int legalAge) {
        this.legalAge = legalAge;
        this.country = country;
    }
}
