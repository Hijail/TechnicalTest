package com.example.technicaltest.mapper;

import com.example.technicaltest.model.Gender;
import org.modelmapper.AbstractConverter;

public class StringGenderConverter extends AbstractConverter<String, Gender> {

    /**
     * Convert String to Gender
     * @param gender
     * @return
     */
    @Override
    protected Gender convert(String gender) {
        if (gender == null) {
            return null;
        }
        return new Gender(gender);
    }
}
