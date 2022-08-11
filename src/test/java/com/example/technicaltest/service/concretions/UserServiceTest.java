package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.InvalidBirthdateException;
import com.example.technicaltest.exception.InvalidCountryException;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CountryRepository countryRepository;
    @InjectMocks
    UserServiceImpl userService;

    private Country country;

    private Country InitCountry(String countryName) {
        country = new Country();
        country.setCountry(countryName);

        return country;
    }

    /**
     * Test create user method
     * with valid birthdate, country ...
     */
    @Test
    public void testCreateUser() {
        final User user = new User("validUser");
        User create = null;

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(new SimpleDateFormat("dd MMM yyyy").format(create.getBirthdate()),
                new SimpleDateFormat("dd MMM yyyy").format(user.getBirthdate()));
        assertEquals(user.getCountry().getCountry(), create.getCountry().getCountry());
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

    /**
     * Test create user method
     * with invalid country
     */
    @Test
    public void testInvalidCountry() {
        final User user = new User("invalidCountry");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("US"));
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("You must be in France", exception.getMessage());
    }

    /**
     * Test create user method
     * with null country
     */
    @Test
    public void testNullCountry() {
        final User user = new User("nullCountry");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(null);
        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> {
            userService.createUser(user);
        });
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }
}
