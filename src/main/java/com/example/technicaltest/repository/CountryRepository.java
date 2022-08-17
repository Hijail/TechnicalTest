package com.example.technicaltest.repository;

import com.example.technicaltest.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCountryName(String country);
}
