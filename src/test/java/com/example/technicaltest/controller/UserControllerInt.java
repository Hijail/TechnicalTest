package com.example.technicaltest.controller;

import com.example.technicaltest.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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

    private UserDTO validUser() {
        final Calendar cal = new GregorianCalendar(2000, Calendar.FEBRUARY, 21);
        LocalDate date = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
        UserDTO user = new UserDTO();

        user.setName("validUser");
        user.setBirthdate(date);
        user.setCountry("France");
        user.setGender("male");
        user.setPhoneNumber("0123456789");
        return user;
    }

    private JSONObject validData() {
        JSONObject data = new JSONObject();

        data.put("id", 5);
        data.put("name", "validUser");
        data.put("birthdate", "2000-02-21");
        data.put("country", "France");
        data.put("gender", "male");
        data.put("phoneNumber", "0123456789");
        return data;
    }

    /**
     * Test post create user with valid user
     * @throws Exception
     */
    @Test
    public void createUser_thenStatus201() throws Exception {
        UserDTO user = validUser();
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

    /**
     * Test post create user with invalid user birthdate
     * @throws Exception
     */
    @Test
    public void createUserInvalidBirthdate_thenStatus400() throws Exception {
        Date input = new Date();
        LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
        UserDTO user = validUser();
        String json;

        user.setBirthdate(date);
        json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        user.setBirthdate(null);
        json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test post create user with invalid user country
     * @throws Exception
     */
    @Test
    public void createUserInvalidCountry_thenStatus400() throws Exception {
        UserDTO user = validUser();
        String json;

        user.setCountry("US");
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        user.setCountry(null);
        json = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test post create user with invalid name
     * @throws Exception
     */
    @Test
    public void createUserInvalidName_thenStatus400() throws Exception {
        UserDTO user = validUser();
        String json;

        user.setName(null);
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test post create user with invalid gender
     * @throws Exception
     */
    @Test
    public void createUserWithInvalidGender_thenStatus400() throws Exception {
        UserDTO user = validUser();
        String json;

        user.setGender("invalid");
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test post create user with invalid phone
     * @throws Exception
     */
    @Test
    public void createUserWithInvalidPhone_thenStatus400() throws Exception {
        UserDTO user = validUser();
        String json;

        user.setPhoneNumber("EAKEAKEJLAKJE");
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test get user with valid id
     * @throws Exception
     */
    @Test
    public void getUser_thenStatus200() throws Exception {
        final Long id = 5L;
        UserDTO user = validUser();
        String json;

        user.setId(id);
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(get("/api/v1/users/{id}", id)
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isOk());
    }

    /**
     * Test get user with invalid id
     * @throws Exception
     */
    @Test
    public void getUserInvalidID_thenStatus404() throws Exception {
        final Long id = 15L;
        UserDTO user = validUser();
        String json;

        user.setId(id);
        json = mapper.writeValueAsString(user);

        this.mockMvc.perform(get("/api/v1/users/{id}", id)
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}
