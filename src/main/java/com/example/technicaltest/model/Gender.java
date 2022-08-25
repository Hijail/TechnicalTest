package com.example.technicaltest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gender")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(position = 1, required = true, value = "male / female / other")
    private String genderType;

    /**
     * Create Gender Object that can be saved in database
     *
     *
     * @param gender Gender Type
     */
    public Gender(String gender) {
        this.genderType = gender;
    }
}
