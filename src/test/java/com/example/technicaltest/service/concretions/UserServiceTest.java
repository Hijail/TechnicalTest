package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.InvalidBirthdateException;
import com.example.technicaltest.model.User;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    /**
     * Test create user method
     * with valid birthdate
     */
    @Test
    public void testValidBirthdate() {
        final UserServiceImpl userService = new UserServiceImpl();
        final User user = new User("validBirthdate");

        user.setBirthdate(new GregorianCalendar(2020, Calendar.FEBRUARY, 21).getTime());
        User create = userService.createUser(user);
        assertEquals(new SimpleDateFormat("dd MMM yyyy").format(create.getBirthdate()),
                new SimpleDateFormat("dd MMM yyyy").format(user.getBirthdate()));
    }

    /**
     * Test create user method
     * with invalid birthdate
     */
    @Test
    public void testInvalidBirthdate() {
        final UserServiceImpl userService = new UserServiceImpl();
        final User user = new User("invalidBirthdate");

        user.setBirthdate(new GregorianCalendar(2020, Calendar.FEBRUARY, 21).getTime());
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("You must be of legal age", exception.getMessage());
    }

    /**
     * Test create user method
     * with null birthdate
     */
    @Test
    public void testNullBirthdate() {
        final UserServiceImpl userService = new UserServiceImpl();
        final User user = new User("nullBirthdate");

        user.setBirthdate(null);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }
}
