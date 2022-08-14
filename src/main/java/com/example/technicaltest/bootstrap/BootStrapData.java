package com.example.technicaltest.bootstrap;

import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.repository.CountryRepository;
import com.example.technicaltest.repository.GenderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final GenderRepository genderRepository;

    /**
     * H2Database constructor.
     * @param countryRepository Country Repository
     * @param genderRepository Gender Repository
     */
    public BootStrapData(CountryRepository countryRepository, GenderRepository genderRepository) {
        this.countryRepository = countryRepository;
        this.genderRepository = genderRepository;
    }

    /**
     * Init H2Database.
     *
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Create product");

        Gender male = new Gender("male");
        genderRepository.save(male);
        Gender female = new Gender("female");
        genderRepository.save(female);
        Gender other = new Gender("other");
        genderRepository.save(other);

        Country france = new Country("France");
        countryRepository.save(france);
    }
}
