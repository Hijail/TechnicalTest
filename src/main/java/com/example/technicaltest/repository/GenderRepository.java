package com.example.technicaltest.repository;

import com.example.technicaltest.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {
    Gender findByGender(String gender);
}
