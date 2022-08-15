package com.example.technicaltest.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "gender")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(position = 1, required = true, value = "male / female / other")
    private String gender;

    public Gender() {
        this.gender = "";
    }

    public Gender(String gender) {
        this.gender = gender;
    }
}
