package com.example.technicaltest.model;

import javax.persistence.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(position = 1, required = true, value = "example: France")
    private String countryName;

    @ApiModelProperty(position = 2, required = true, value = "example: 18")
    private int legalAge;

    public Country() {
        this.legalAge = 0;
        this.countryName = "";
    }

    /**
     * Create Country Object that can be saved in database
     *
     * Legal age was here to simplify the country legal check
     *
     * @param country Country Name
     * @param legalAge Legal Age in country
     */
    public Country(String country, int legalAge) {
        this.legalAge = legalAge;
        this.countryName = country;
    }
}
