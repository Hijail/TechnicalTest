package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.InvalidBirthdateException;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    /**
     * Test create user method
     * with valid birthdate
     */
    @Test
    public void testValidBirthdate() {
        final User user = new User("validBirthdate");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        User create = null;
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(new SimpleDateFormat("dd MMM yyyy").format(create.getBirthdate()),
                new SimpleDateFormat("dd MMM yyyy").format(user.getBirthdate()));
    }

    /**
     * Test create user method
     * with invalid birthdate
     */
    @Test
    public void testInvalidBirthdate() {
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
        final User user = new User("nullBirthdate");

        user.setBirthdate(null);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }
}
