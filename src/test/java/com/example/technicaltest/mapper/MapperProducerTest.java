package com.example.technicaltest.mapper;

import com.example.technicaltest.dto.UserDTO;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

public class MapperProducerTest {

    private ModelMapper mapper;

    /**
     * Set up mapper
     */
    @BeforeEach
    public void setup() {
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
     * Test convert User to UserDTO with  all fields
     */
    @Test
    public void whenMapUserWithExactMatch_thenConvertsToDTO() {
        LocalDate date = LocalDate.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        User user = new User(1L, "user", date, new Country("France", 18),
                new Gender("male"), "0123456789");
        UserDTO userDTO = this.mapper.map(user, UserDTO.class);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getBirthdate(), userDTO.getBirthdate());
        assertEquals(user.getCountry().getCountryName(), userDTO.getCountry());
        assertEquals(user.getGender().getGenderType(), userDTO.getGender());
        assertEquals(user.getPhoneNumber(), userDTO.getPhoneNumber());
    }

    /**
     * Test convert UserDTO to User with  all fields
     */
    @Test
    public void whenMapUserDTOWithExactMatch_thenConvertsToEntity() {
        LocalDate date = LocalDate.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        UserDTO userDTO = new UserDTO();

        userDTO.setId(1L);
        userDTO.setName("user");
        userDTO.setCountry("France");
        userDTO.setGender("male");
        userDTO.setPhoneNumber("0123456789");
        userDTO.setBirthdate(date);
        User user = this.mapper.map(userDTO, User.class);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getBirthdate(), user.getBirthdate());
        assertEquals(userDTO.getCountry(), user.getCountry().getCountryName());
        assertEquals(userDTO.getGender(), user.getGender().getGenderType());
        assertEquals(userDTO.getPhoneNumber(), user.getPhoneNumber());
    }

    /**
     * Test convert User to UserDTO with  country and gender null
     */
    @Test
    public void whenMapUserDTOWithNullExactMatch_thenConvertsToEntity() {
        LocalDate date = LocalDate.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
        UserDTO userDTO = new UserDTO();

        userDTO.setId(1L);
        userDTO.setName("user");
        userDTO.setCountry(null);
        userDTO.setGender(null);
        userDTO.setPhoneNumber("0123456789");
        userDTO.setBirthdate(date);
        User user = this.mapper.map(userDTO, User.class);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getName(), user.getName());
        assertEquals(userDTO.getBirthdate(), user.getBirthdate());
        assertEquals(userDTO.getCountry(), user.getCountry());
        assertEquals(userDTO.getGender(), user.getGender());
        assertEquals(userDTO.getPhoneNumber(), user.getPhoneNumber());
    }
}
