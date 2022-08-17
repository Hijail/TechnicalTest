package com.example.technicaltest.controller;

import com.example.technicaltest.TechnicalTestApplication;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
import com.example.technicaltest.repository.GenderRepository;
import com.example.technicaltest.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
public class UserControllerInt {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private User validUser() {
        final Calendar cal = new GregorianCalendar(2000, Calendar.FEBRUARY, 21);
        User user = new User("validUser", cal.getTime(), new Country("France", 18));

        user.setGender(new Gender("male"));
        user.setPhoneNumber("0123456789");
        return user;
    }

    private JSONObject validData() {
        JSONObject data = new JSONObject();
        Country france = new Country("France", 18);
        Gender male = new Gender("male");

        male.setId(2L);
        france.setId(1L);
        data.put("id", 5);
        data.put("name", "validUser");
        data.put("birthdate", "2000-02-21T00:00:00.000+00:00");
        data.put("country", france);
        data.put("gender", male);
        data.put("phoneNumber", "0123456789");
        return data;
    }

    @Test
    public void createUser_thenStatus201() throws Exception {
        User user = validUser();
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", validData());
        responseDetails.put("status", HttpStatus.CREATED.value());
        responseDetails.put("message", "User Created");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(responseDetails.toString()));
    }

    @Test
    public void createUserInvalidBirthdate_thenStatus403() throws Exception {
        User user = new User("Jean", new Date(), new Country("France", 18));
        String json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

        user.setBirthdate(null);
        json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserInvalidCountry_thenStatus403() throws Exception {
        User user = validUser();
        String json;

        user.setCountry(new Country("US", 21));
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());

        user.setCountry(null);
        json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserInvalidName_thenStatus403() throws Exception {
        User user = validUser();
        String json;

        user.setName(null);
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserWithInvalidGender_thenStatus400() throws Exception {
        User user = validUser();
        String json;

        user.setGender(new Gender("invalid"));
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserWithInvalidPhone_thenStatus400() throws Exception {
        User user = validUser();
        String json;

        user.setPhoneNumber("EAKEAKEJLAKJE");
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
