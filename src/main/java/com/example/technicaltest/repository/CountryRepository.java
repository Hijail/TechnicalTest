package com.example.technicaltest.repository;

import com.example.technicaltest.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCountry(String country);
}
