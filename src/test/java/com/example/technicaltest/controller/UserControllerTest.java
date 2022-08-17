package com.example.technicaltest.controller;

import com.example.technicaltest.exception.*;
import com.example.technicaltest.model.Country;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class User Controller Test
 * @author Baptiste Gellato
 * @version 0.0.1
 */
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IUserService service;

    private User validUser() {
        final Calendar cal = new GregorianCalendar(2000, Calendar.FEBRUARY, 21);

        return new User("validUser", cal.getTime(), new Country("France", 18));
    }

    private JSONObject validData() {
        JSONObject data = new JSONObject();

        data.put("id", null);
        data.put("name", "validUser");
        data.put("birthdate", "2000-02-20T23:00:00.000+00:00");
        data.put("country", new Country("France", 18));
        data.put("gender", null);
        data.put("phoneNumber", null);
        return data;
    }
    /**
     * Test create user route
     *
     * @throws Exception if route doesn't return created status
     */
    @Test
    public void createUserTest() throws Exception {
        User user = validUser();
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", validData());
        responseDetails.put("status", HttpStatus.CREATED.value());
        responseDetails.put("message", "User Created");
        given(service.createUser(any(User.class))).willReturn(user);
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isCreated())
                        .andExpect(content().json(responseDetails.toString()));
    }

    @Test
    public void createUserTestFailBirthdate() throws Exception {
        User user = validUser();
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.FORBIDDEN.value());
        responseDetails.put("message", "Null parameters are not allowed");
        given(service.createUser(any(User.class))).willThrow(new InvalidBirthdateException("Null parameters are not allowed"),
                new InvalidBirthdateException("You must be of legal age"));
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "You must be of legal age");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(content().json(responseDetails.toString()));

    }

    @Test
    public void createUserTestFailCountry() throws Exception {
        User user = validUser();
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.FORBIDDEN.value());
        responseDetails.put("message", "Null parameters are not allowed");
        given(service.createUser(any(User.class))).willThrow(new InvalidCountryException("Null parameters are not allowed"),
                new InvalidCountryException("You must be in France"));
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(content().json(responseDetails.toString()));

        responseDetails.remove("message");
        responseDetails.put("message", "You must be in France");
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(content().json(responseDetails.toString()));
    }

    @Test
    public void createUserTestFailGender() throws Exception {
        User user = validUser();
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.BAD_REQUEST.value());
        responseDetails.put("message", "Only male / female / other or empty are allow for gender");
        given(service.createUser(any(User.class))).willThrow(
                new InvalidGenderException("Only male / female / other or empty are allow for gender"));
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseDetails.toString()));
    }

    @Test
    public void createUserTestFailPhone() throws Exception {
        User user = validUser();
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.BAD_REQUEST.value());
        responseDetails.put("message", "Invalid phone number");
        given(service.createUser(any(User.class))).willThrow(new InvalidPhoneException("Invalid phone number"));
        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseDetails.toString()));
    }

    /**
     * Test create user route with bad content
     *
     * @throws Exception if route doesn't return correct response
     */
    @Test
    public void createUserTestFailNullUser() throws Exception {
        final Date birthdate = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        User user = new User("Jean", birthdate, new Country("France", 18));
        String json = mapper.writeValueAsString(user);
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.FORBIDDEN.value());
        responseDetails.put("message", "Null parameters are not allowed");
        given(service.createUser(any(User.class))).willThrow(new InvalidUsernameException("Null parameters are not allowed"));

        this.mockMvc.perform(post("/api/v1/users")
                        .content(json)
                        .contentType("application/json"))
                        .andExpect(status().isForbidden())
                        .andExpect(content().json(responseDetails.toString()));
    }

    /**
     * Test get user by id route
     *
     * @throws Exception if route doesn't return correct user
     */
    @Test
    public void getUserByIdTest() throws Exception {
        final Long id = 1L;
        User user = validUser();
        JSONObject responseDetails = new JSONObject();
        JSONObject data = validData();

        user.setId(id);
        data.put("id", user.getId());
        responseDetails.put("data", data);
        responseDetails.put("status", HttpStatus.OK.value());
        responseDetails.put("message", "");
        System.out.println(responseDetails.toString());
        given(service.getUserById(id)).willReturn(user);

        this.mockMvc.perform(get("/api/v1/users/{id}", id)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(responseDetails.toString()));
    }

    /**
     * Test get user by id error
     *
     * @throws Exception if route doesn't return Bad Request status
     */
    @Test
    public void getUserByIdErrorTest() throws Exception {
        final Long id = 1L;
        JSONObject responseDetails = new JSONObject();

        responseDetails.put("data", null);
        responseDetails.put("status", HttpStatus.BAD_REQUEST.value());
        responseDetails.put("message", "Invalid UserId");

        given(service.getUserById(id)).willThrow(new UserException("Invalid UserId"));

        this.mockMvc.perform(get("/api/v1/users/{id}", id)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().json(responseDetails.toString()));
    }
}