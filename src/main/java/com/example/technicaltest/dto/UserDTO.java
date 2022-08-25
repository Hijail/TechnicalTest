package com.example.technicaltest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {
    private Long        id;
    private String      name;
    private LocalDate   birthdate;
    private String      country;
    private String      gender;
    private String      phoneNumber;
}
