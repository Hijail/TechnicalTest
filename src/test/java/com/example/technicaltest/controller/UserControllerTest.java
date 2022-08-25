package com.example.technicaltest.controller;

import com.example.technicaltest.dto.UserDTO;
import com.example.technicaltest.exception.*;
import com.example.technicaltest.service.abstractions.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.technicaltest.response.ResponseHandler.generateResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * Class User Controller Test
 * @author Baptiste Gellato
 * @version 0.0.1
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    private IUserService service;

    private UserDTO validUser() {
        final Calendar cal = new GregorianCalendar(2000, Calendar.FEBRUARY, 10);
        LocalDate date = LocalDate.ofInstant(cal.toInstant(), ZoneId.systemDefault());
        UserDTO user = new UserDTO();

        user.setName("validUser");
        user.setCountry("France");
        user.setBirthdate(date);
        return user;
    }

    /**
     * Test create user route
     *
     * @throws Exception if route doesn't return created status
     */
    @Test
    public void createUserTest() throws Exception {
        UserDTO user = validUser();
        ResponseEntity<Object> expect = generateResponse("User Created", HttpStatus.CREATED, user);

        given(service.createUser(any(UserDTO.class))).willReturn(user);
        ResponseEntity<Object> responseEntity = userController.createUser(user);
        assertTrue(expect.equals(responseEntity));
    }

    /**
     * Test create user with invalid birthdate
     *
     * @throws Exception if route doesn't return correct status
     */
    @Test
    public void createUserTestFailBirthdate() throws Exception {
        UserDTO user = validUser();

        given(service.createUser(any(UserDTO.class))).willThrow(new InvalidBirthdateException("Null parameters are not allowed"),
                new InvalidBirthdateException("You must be of legal age"));

        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> userController.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
        InvalidBirthdateException exception2 = assertThrows(InvalidBirthdateException.class, () -> userController.createUser(user));
        assertEquals("You must be of legal age", exception2.getMessage());
    }

    /**
     * Test create user with invalid country
     *
     * @throws Exception if route doesn't return correct status
     */
    @Test
    public void createUserTestFailCountry() throws Exception {
        UserDTO user = validUser();

        given(service.createUser(any(UserDTO.class))).willThrow(new InvalidCountryException("Null parameters are not allowed"),
                new InvalidCountryException("You must be in France"));
        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> userController.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
        InvalidCountryException exception2 = assertThrows(InvalidCountryException.class, () -> userController.createUser(user));
        assertEquals("You must be in France", exception2.getMessage());
    }

    /**
     * Test create user with invalid gender
     *
     * @throws Exception if route doesn't return correct status
     */
    @Test
    public void createUserTestFailGender() throws Exception {
        UserDTO user = validUser();
        ResponseEntity<Object> expect = generateResponse(
                "Only male / female / other or empty are allow for gender",
                HttpStatus.BAD_REQUEST, null);

        given(service.createUser(any(UserDTO.class))).willThrow(
                new InvalidGenderException("Only male / female / other or empty are allow for gender"));
        InvalidGenderException exception = assertThrows(InvalidGenderException.class, () -> userController.createUser(user));
        assertEquals("Only male / female / other or empty are allow for gender", exception.getMessage());
    }

    /**
     * Test create user with invalid phone
     *
     * @throws Exception if route doesn't return correct status
     */
    @Test
    public void createUserTestFailPhone() throws Exception {
        UserDTO user = validUser();

        given(service.createUser(any(UserDTO.class))).willThrow(new InvalidPhoneException("Invalid phone number"));
        InvalidPhoneException exception = assertThrows(InvalidPhoneException.class, () -> userController.createUser(user));
        assertEquals("Invalid phone number", exception.getMessage());
    }

    /**
     * Test create user route with bad content
     *
     * @throws Exception if route doesn't return correct response
     */
    @Test
    public void createUserTestFailNullUser() throws Exception {
        UserDTO user = validUser();

        given(service.createUser(any(UserDTO.class))).willThrow(new InvalidUsernameException("Null parameters are not allowed"));
        InvalidUsernameException exception = assertThrows(InvalidUsernameException.class, () -> userController.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Test get user by id route
     *
     * @throws Exception if route doesn't return correct user
     */
    @Test
    public void getUserByIdTest() throws Exception {
        final Long id = 1L;
        UserDTO user = validUser();
        ResponseEntity<Object> expect;

        user.setId(id);
        expect = generateResponse("",
                HttpStatus.OK, user);
        given(service.getUserById(id)).willReturn(user);
        ResponseEntity<Object> responseEntity  = userController.getUserById(id);
        assertTrue(expect.equals(responseEntity));
    }

    /**
     * Test get user by id error
     *
     * @throws Exception if route doesn't return correct status
     */
    @Test
    public void getUserByIdErrorTest() throws Exception {
        final Long id = 1L;
        ResponseEntity<Object> expect = generateResponse("Invalid UserId",
                HttpStatus.FORBIDDEN, null);

        given(service.getUserById(id)).willThrow(new UserException("Invalid UserId"));
        UserException exception = assertThrows(UserException.class, () -> userController.getUserById(id));
        assertEquals("Invalid UserId", exception.getMessage());
    }
}