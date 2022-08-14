package com.example.technicaltest.controller;

import com.example.technicaltest.exception.*;
import com.example.technicaltest.model.User;
import com.example.technicaltest.service.abstractions.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IUserService service;

    /**
     * Test create user route
     *
     * @throws Exception if route doesn't return created status
     */
    @Test
    public void createUserTest() throws Exception {
        User user = new User("Jean");
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", user);
        responseDetails.put("status", HttpStatus.CREATED.value());
        responseDetails.put("message", "User Created");
        given(service.createUser(user)).willReturn(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(responseDetails.toString()));
    }

    @Test
    public void createUserTestFailBadContent() throws Exception {
        User user = new User("Jean");
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.BAD_REQUEST.value());
        responseDetails.put("message", "Null parameters are not allowed");
        given(service.createUser(user)).willThrow(new InvalidBirthdateException("Null parameters are not allowed"),
                new InvalidBirthdateException("You must be of legal age"),
                new InvalidCountryException("Null parameters are not allowed"),
                new InvalidCountryException("You must be in France"),
                new InvalidGenderException("Only male / female / other or empty are allow for gender"),
                new InvalidUsernameException("Username already exist"),
                new InvalidPhoneException("Invalid phone number"));
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "You must be of legal age");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "Null parameters are not allowed");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "You must be in France");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "Only male / female / other or empty are allow for gender");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "Username already exist");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "Invalid phone number");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));
    }
}