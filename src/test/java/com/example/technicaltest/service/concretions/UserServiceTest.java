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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        country.setCountryName(countryName);

        return country;
    }

    /**
     * Init Gender Test
     *
     * @return new Gender
     */
    private Gender InitGender(String genderType) {
        Gender gender = new Gender();
        gender.setGenderType(genderType);

        return gender;
    }

    /**
     * Init valid User Test
     *
     * @return new valid User
     */
    private User validUser() {
        final Date birthdate = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();

        return new User("validUser", birthdate, InitCountry("France"));
    }

    /**
     * Test create user method
     * with valid birthdate, country ...
     */
    @Test
    public void testCreateUser() {
        final User user = validUser();
        User create;

        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(new SimpleDateFormat("dd MMM yyyy").format(create.getBirthdate()),
                new SimpleDateFormat("dd MMM yyyy").format(user.getBirthdate()));
        assertEquals(user.getCountry().getCountryName(), create.getCountry().getCountryName());
        assertEquals(user.getName(), create.getName());
    }

    /**
     * Test create user method
     * with invalid birthdate
     */
    @Test
    public void testInvalidBirthdate() {
        Country france = new Country("France", 18);
        Date birthdate = new Date();
        User user = new User("invalidBirthdate", birthdate, france);

        given(countryRepository.findByCountryName("France")).willReturn(france);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> userService.createUser(user));
        assertEquals("You must be of legal age", exception.getMessage());
    }

    /**
     * Test create user method
     * with null birthdate
     */
    @Test
    public void testNullBirthdate() {
        Country france = new Country("France", 18);
        User user = new User("invalidBirthdate", null, france);

        given(countryRepository.findByCountryName("France")).willReturn(france);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class, () -> userService.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Test create user method
     * with invalid country
     */
    @Test
    public void testInvalidCountry() {
        final Date birthdate = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        final User user = new User("invalidCountry", birthdate, InitCountry("US"));

        given(countryRepository.findByCountryName("US")).willReturn(null);
        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> userService.createUser(user));
        assertEquals("You must be in France", exception.getMessage());
    }

    /**
     * Test create user method
     * with null country
     */
    @Test
    public void testNullCountry() {
        final Date birthdate = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        final User user = new User("invalidCountry", birthdate, null);

        InvalidCountryException exception = assertThrows(InvalidCountryException.class, () -> userService.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Test create user method
     * with male gender
     */
    @Test
    public void testFirstValidGender() {
        final User user = validUser();
        User create;
        Gender male = InitGender("male");

        user.setGender(male);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(genderRepository.findByGenderType("male")).willReturn(male);
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGenderType(), create.getGender().getGenderType());
    }

    /**
     * Test create user method
     * with female gender
     */
    @Test
    public void testSecondValidGender() {
        final User user = validUser();
        User create;
        Gender female = InitGender("female");

        user.setGender(female);
        given(genderRepository.findByGenderType("female")).willReturn(female);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGenderType(), create.getGender().getGenderType());
    }

    /**
     * Test create user method
     * with other gender
     */
    @Test
    public void testThirdValidGender() {
        final User user = validUser();
        User create;
        Gender other = InitGender("other");

        user.setGender(other);
        given(genderRepository.findByGenderType("other")).willReturn(other);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGenderType(), create.getGender().getGenderType());
    }

    /**
     * Test create user method
     * with null gender
     */
    @Test
    public void testNullGender() {
        final User user = validUser();
        User create;

        user.setGender(null);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userRepository.save(user)).willReturn(user);
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
        final User user = validUser();
        Gender invalid = InitGender("invalid");

        user.setGender(invalid);
        given(genderRepository.findByGenderType("invalid")).willReturn(null);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        InvalidGenderException exception = assertThrows(InvalidGenderException.class, () -> userService.createUser(user));
        assertEquals("Only male / female / other or empty are allow for gender", exception.getMessage());
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
        final User user = validUser();
        int index = 0;

        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        for(String phoneNumber : validPhoneNumbers) {
            user.setPhoneNumber(phoneNumber);
            user.setName("user_" + index);
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
        final User user = validUser();
        int index = 0;
        User create;

        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        try {
            for(String phoneNumber : validPhoneNumbers) {
                user.setPhoneNumber(phoneNumber);
                user.setName("user_" + index);
                given(userRepository.save(user)).willReturn(user);
                create = userService.createUser(user);
                assertEquals(user.getName(), create.getName());
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
        final User user = validUser();

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

        given(userRepository.findById(id)).willReturn(Optional.empty());
        UserException exception = assertThrows(UserException.class, () -> userService.getUserById(id));
        assertEquals("Invalid UserId", exception.getMessage());
    }


    /**
     * Check null name
     */
    @Test
    public void checkNameIsNull() {
        final User user = validUser();

        user.setName(null);
        InvalidUsernameException exception = assertThrows(InvalidUsernameException.class, () -> userService.createUser(user));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Check create user with id define
     */
    @Test
    public void checkUserWithId() {
        final User user = validUser();
        final User user2 = validUser();
        User created;

        user2.setId(1L);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userRepository.save(any(User.class))).willReturn(user);
        userService.createUser(user);
        created = userService.createUser(user2);
        assertEquals(created.getId(), null);
    }
}
