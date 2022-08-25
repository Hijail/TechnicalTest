package com.example.technicaltest.mapper;

import com.example.technicaltest.model.Gender;
import org.modelmapper.AbstractConverter;

public class GenderStringConverter extends AbstractConverter<Gender, String> {

    /**
     * Gender to string converter
     *
     * @param gender
     * @return
     */
    @Override
    protected String convert(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.getGenderType();
    }
}
