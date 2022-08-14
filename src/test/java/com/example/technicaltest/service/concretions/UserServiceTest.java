package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.*;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
import com.example.technicaltest.repository.GenderRepository;
import com.example.technicaltest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

/**
 * Class User Service Test
 * @author Baptiste Gellato
 * @version 0.0.1
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GenderRepository genderRepository;
    @Mock
    CountryRepository countryRepository;
    @InjectMocks
    UserServiceImpl userService;

    /**
     * Init Country Test
     *
     * @return new Country
     */
    private Country InitCountry(String countryName) {
        Country country = new Country();
        country.setCountry(countryName);

        return country;
    }

    /**
     * Init Gender Test
     *
     * @return new Gender
     */
    private Gender InitGender(String genderType) {
        Gender gender = new Gender();
        gender.setGender(genderType);

        return gender;
    }

    /**
     * Test create user method
     * with valid birthdate, country ...
     */
    @Test
    public void testCreateUser() {
        final User user = new User("validUser");
        User create;

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
        assertEquals(user.getUsername(), create.getUsername());
    }

    /**
     * Test create user method
     * with invalid birthdate
     */
    @Test
    public void testInvalidBirthdate() {
        final User user = new User("invalidBirthdate");
        Country france = new Country("France", 18);

        given(countryRepository.findByCountry("France")).willReturn(france);
        user.setCountry(france);
        user.setBirthdate(new GregorianCalendar(2020, Calendar.FEBRUARY, 21).getTime());
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> userService.createUser(user));
        assertEquals("You must be of legal age", exception.getMessage());
    }

    /**
     * Test create user method
     * with null birthdate
     */
    @Test
    public void testNullBirthdate() {
        final User user = new User("nullBirthdate");
        Country france = new Country("France", 18);

        given(countryRepository.findByCountry("France")).willReturn(france);
        user.setCountry(france);
        user.setBirthdate(null);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> userService.createUser(user));
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
        given(countryRepository.findByCountry("US")).willReturn(null);
        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> userService.createUser(user));
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
        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> userService.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Test create user method
     * with male gender
     */
    @Test
    public void testFirstValidGender() {
        final User user = new User("validUser");
        User create;
        Gender male = InitGender("male");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        user.setGender(male);
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        given(genderRepository.findByGender("male")).willReturn(male);
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGender(), create.getGender().getGender());
    }

    /**
     * Test create user method
     * with female gender
     */
    @Test
    public void testSecondValidGender() {
        final User user = new User("validUser");
        User create;
        Gender female = InitGender("female");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        user.setGender(female);
        given(genderRepository.findByGender("female")).willReturn(female);
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGender(), create.getGender().getGender());
    }

    /**
     * Test create user method
     * with other gender
     */
    @Test
    public void testThirdValidGender() {
        final User user = new User("validUser");
        User create;
        Gender other = InitGender("other");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        user.setGender(other);
        given(genderRepository.findByGender("other")).willReturn(other);
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGender(), create.getGender().getGender());
    }

    /**
     * Test create user method
     * with null gender
     */
    @Test
    public void testNullGender() {
        final User user = new User("validUser");
        User create;

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        user.setGender(null);
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender(), create.getGender());
    }

    /**
     * Test create user method
     * with invalid gender
     */
    @Test
    public void testInvalidGender() {
        final User user = new User("validUser");
        Gender invalid = InitGender("invalid");

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        user.setGender(invalid);
        given(genderRepository.findByGender("invalid")).willReturn(null);
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        InvalidGenderException exception = assertThrows(InvalidGenderException.class, () -> userService.createUser(user));
        assertEquals("Only male / female / other or empty are allow for gender", exception.getMessage());
    }

    /**
     * Test create user method
     * with existing username
     */
    @Test
    public void testUsernameAlreadyExist() {
        final User user = new User("validUser");

        given(userRepository.findByUsername("validUser")).willReturn(user);
        InvalidUsernameException exception = assertThrows(InvalidUsernameException.class, () -> userService.createUser(user));
        assertEquals("Username already exist", exception.getMessage());
    }

    /**
     * Test create user method
     * with invalid phone number
     */
    @Test
    public void testInvalidPhone() {
        String[] validPhoneNumbers
                = {"20555501256","202 555 A0125", "(202) 555--0125", "+111 (202) 555%-0125",
                "6%6 856 789", "+11$ 636 856 789", "636 8A 67 89", "+111 6(6 85 67 89"};
        final User user = new User("validUser");
        int index = 0;

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        for(String phoneNumber : validPhoneNumbers) {
            user.setPhoneNumber(phoneNumber);
            user.setUsername("user_" + index);
            InvalidPhoneException exception = assertThrows(InvalidPhoneException.class, () -> userService.createUser(user));
            assertEquals("Invalid phone number", exception.getMessage());
            index ++;
        }
    }

    /**
     * Test create user method
     * with correct phone number
     */
    @Test
    public void whenMatchesPhoneNumber_thenCorrect() {
        String[] validPhoneNumbers
                = {"2055550125","202 555 0125", "(202) 555-0125", "+111 (202) 555-0125",
                "636 856 789", "+111 636 856 789", "636 85 67 89", "+111 636 85 67 89", null};
        final User user = new User("validUser");
        int index = 0;
        User create;

        user.setBirthdate(new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime());
        user.setCountry(InitCountry("France"));
        given(countryRepository.findByCountry("France")).willReturn(InitCountry("France"));
        try {
            for(String phoneNumber : validPhoneNumbers) {
                user.setPhoneNumber(phoneNumber);
                user.setUsername("user_" + index);
                create = userService.createUser(user);
                assertEquals(user.getUsername(), create.getUsername());
                assertEquals(user.getPhoneNumber(), create.getPhoneNumber());
                index ++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test get user by id method
     * fail if user is null
     */
    @Test
    public void testGetUserByIdOk() {
        User user = new User("Jean");

        user.setId(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        final Optional<User> expected = Optional.ofNullable(userService.getUserById(1L));

        assertThat(expected).isNotNull();
    }

    /**
     * Test get user by id method
     * fail if method doesn't throw
     */
    @Test
    public void testGetUserByIdFail() {
        final Long id = 1L;
        User p1 = null;

        given(userRepository.findById(id)).willReturn(Optional.ofNullable(p1));
        UserException exception = assertThrows(UserException.class, () -> userService.getUserById(id));
        assertEquals("Invalid UserId", exception.getMessage());
    }
}
