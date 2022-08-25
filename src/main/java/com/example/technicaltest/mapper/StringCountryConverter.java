package com.example.technicaltest.mapper;

import com.example.technicaltest.model.Country;
import org.modelmapper.AbstractConverter;

public class StringCountryConverter extends AbstractConverter<String, Country> {

    /**
     * Convert String to Country
     * @param country
     * @return
     */
    @Override
    protected Country convert(String country) {
        if (country == null) {
            return null;
        }
        return new Country(country, 0);
    }
}
