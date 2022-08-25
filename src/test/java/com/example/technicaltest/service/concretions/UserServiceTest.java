package com.example.technicaltest.service.concretions;

import com.example.technicaltest.dto.UserDTO;
import com.example.technicaltest.exception.*;
import com.example.technicaltest.mapper.*;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
import com.example.technicaltest.repository.GenderRepository;
import com.example.technicaltest.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

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

    @Mock
    ModelMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    private ModelMapper mapper;

    /**
     * Set up mapper
     */
    @BeforeEach
    public void setup() {
        mapper = new ModelMapper();
        this.mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        this.mapper.addConverter(new CountryStringConverter());
        this.mapper.addConverter(new StringCountryConverter());
        this.mapper.addConverter(new StringGenderConverter());
        this.mapper.addConverter(new GenderStringConverter());
    }

    /**
     * Init Country Test
     *
     * @return new Country
     */
    private Country InitCountry(String countryName) {
        Country country = new Country(1L, countryName, 18);

        return country;
    }

    /**
     * Init Gender Test
     *
     * @return new Gender
     */
    private Gender InitGender(String genderType) {
        Gender gender;

        if (genderType == null) {
            return null;
        }
        gender = new Gender(1L, genderType);
        return gender;
    }

    /**
     * Init valid User Test
     *
     * @return new valid User
     */
    private User validUser() {
        final Date input = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());

        return new User("validUser", date, InitCountry("France"));
    }

    /**
     * Init valid User Test with all parameter
     *
     * @return new valid User
     */
    private User validUser(Long id, String username, String gender, String phone) {
        final Date input = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());

        return new User(id,username, date, InitCountry("France"),
                InitGender(gender), phone);
    }

    /**
     * Test create user method
     * with valid birthdate, country ...
     */
    @Test
    public void testCreateUser() {
        final User user = validUser();
        UserDTO create;

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(mapper.map(user, UserDTO.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getBirthdate(), create.getBirthdate());
        assertEquals(user.getCountry().getCountryName(), create.getCountry());
        assertEquals(user.getName(), create.getName());
    }

    /**
     * Test create user method
     * with invalid birthdate
     */
    @Test
    public void testInvalidBirthdate() {
        Country france = new Country("France", 18);
        Date input = new Date();
        LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
        User user = new User("invalidBirthdate", date, france);

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(countryRepository.findByCountryName("France")).willReturn(france);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class,
                () -> userService.createUser(mapper.map(user, UserDTO.class)));
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

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(countryRepository.findByCountryName("France")).willReturn(france);
        InvalidBirthdateException exception = assertThrows(InvalidBirthdateException.class,
                () -> userService.createUser(mapper.map(user, UserDTO.class)));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Test create user method
     * with invalid country
     */
    @Test
    public void testInvalidCountry() {
        final Date input = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
        final User user = new User("invalidCountry", date, InitCountry("US"));

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(countryRepository.findByCountryName("US")).willReturn(null);
        InvalidCountryException exception = assertThrows(InvalidCountryException.class,
                () -> userService.createUser(mapper.map(user, UserDTO.class)));
        assertEquals("You must be in France", exception.getMessage());
    }

    /**
     * Test create user method
     * with null country
     */
    @Test
    public void testNullCountry() {
        final Date input = new GregorianCalendar(2000, Calendar.FEBRUARY, 21).getTime();
        LocalDate date = LocalDate.ofInstant(input.toInstant(), ZoneId.systemDefault());
        final User user = new User("invalidCountry", date, null);

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        InvalidCountryException exception = assertThrows(InvalidCountryException.class,
                () -> userService.createUser(mapper.map(user, UserDTO.class)));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Test create user method
     * with male gender
     */
    @Test
    public void testFirstValidGender() {
        final User user = validUser(null, "user", "male", null);
        UserDTO create;

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(genderRepository.findByGenderType("male")).willReturn(InitGender("male"));
        given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(mapper.map(user, UserDTO.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGenderType(), create.getGender());
    }

    /**
     * Test create user method
     * with female gender
     */
    @Test
    public void testSecondValidGender() {
        final User user = validUser(null, "user", "female", null);
        UserDTO create;

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(genderRepository.findByGenderType("female")).willReturn(InitGender("female"));
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(mapper.map(user, UserDTO.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGenderType(), create.getGender());
    }

    /**
     * Test create user method
     * with other gender
     */
    @Test
    public void testThirdValidGender() {
        final User user = validUser(null, "user", "other", null);
        UserDTO create;

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(genderRepository.findByGenderType("other")).willReturn(InitGender("other"));
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(mapper.map(user, UserDTO.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(user.getGender().getGenderType(), create.getGender());
    }

    /**
     * Test create user method
     * with null gender
     */
    @Test
    public void testNullGender() {
        final User user = validUser(null, "user", null, null);
        UserDTO create;

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
        given(userRepository.save(user)).willReturn(user);
        try {
            create = userService.createUser(mapper.map(user, UserDTO.class));
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
        final User user = validUser(null, "user", "invalid", null);

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        given(genderRepository.findByGenderType("invalid")).willReturn(null);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        InvalidGenderException exception = assertThrows(InvalidGenderException.class,
                () -> userService.createUser(mapper.map(user, UserDTO.class)));
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
        User user;
        int index = 0;

        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        for(String phoneNumber : validPhoneNumbers) {
            user = validUser(null, "user_" + index, null, phoneNumber);
            User finalUser = user;
            given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(finalUser);
            InvalidPhoneException exception = assertThrows(InvalidPhoneException.class,
                    () -> userService.createUser(mapper.map(finalUser, UserDTO.class)));
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
        User user;
        int index = 0;
        UserDTO create;

        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        try {
            for(String phoneNumber : validPhoneNumbers) {
                user = validUser(null, "user_" + index, null, phoneNumber);
                given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
                given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
                given(userRepository.save(user)).willReturn(user);
                create = userService.createUser(mapper.map(user, UserDTO.class));
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

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        final Optional<UserDTO> expected = Optional.ofNullable(userService.getUserById(1L));

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
        final User user = validUser(null, null, null, null);

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user);
        InvalidUsernameException exception = assertThrows(InvalidUsernameException.class,
                () -> userService.createUser(mapper.map(user, UserDTO.class)));
        assertEquals("Null parameters are not allowed", exception.getMessage());
    }

    /**
     * Check create user with id define
     */
    @Test
    public void checkUserWithId() {
        final User user = validUser();
        final User user2 = validUser(1L, "user", null, null);
        UserDTO created;

        given(userMapper.map(any(UserDTO.class), eq(User.class))).willReturn(user, user2);
        given(countryRepository.findByCountryName("France")).willReturn(InitCountry("France"));
        given(userMapper.map(user, UserDTO.class)).willReturn(mapper.map(user, UserDTO.class));
        given(userRepository.save(any(User.class))).willReturn(user);
        userService.createUser(mapper.map(user, UserDTO.class));
        created = userService.createUser(mapper.map(user2, UserDTO.class));
        assertEquals(created.getId(), null);
    }
}
