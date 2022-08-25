package com.example.technicaltest.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.function.Supplier;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Component
public class MapperProducer implements Supplier<ModelMapper> {

    private ModelMapper mapper;

    /**
     * Initialize mapper
     */
    @PostConstruct
    public void init() {
        this.mapper = new ModelMapper();
        this.mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        this.mapper.addConverter(new CountryStringConverter());
        this.mapper.addConverter(new StringCountryConverter());
        this.mapper.addConverter(new StringGenderConverter());
        this.mapper.addConverter(new GenderStringConverter());
    }


    /**
     * Get mapper
     * @return mapper
     */
    @Override
    @Bean
    public ModelMapper get() {
        return mapper;
    }
}
