package com.example.technicaltest.mapper;

import org.modelmapper.AbstractConverter;

import com.example.technicaltest.model.Country;

public class CountryStringConverter extends AbstractConverter<Country, String> {

    /**
     * Convert Country to String
     *
     * @param country
     * @return
     */
    @Override
    protected String convert(Country country) {
        if (country == null) {
            return null;
        }
        return country.getCountryName();
    }
}
