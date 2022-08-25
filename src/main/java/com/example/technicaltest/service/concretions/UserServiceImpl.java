package com.example.technicaltest.service.concretions;

import com.example.technicaltest.log.annotation.Supervision;
import com.example.technicaltest.dto.UserDTO;
import com.example.technicaltest.exception.*;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
import com.example.technicaltest.repository.GenderRepository;
import com.example.technicaltest.repository.UserRepository;
import com.example.technicaltest.service.abstractions.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class User Service
 * @author Baptiste Gellato
 * @version 0.0.1
 */
@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;

    private final CountryRepository countryRepository;

    private final GenderRepository genderRepository;

    private final ModelMapper mapper;

    /**
     * User service constructor
     * Init user service repository
     *
     * @param userRepository User repository
     * @param countryRepository Country repository
     * @param genderRepository Gender repository
     */
    public UserServiceImpl(UserRepository userRepository, CountryRepository countryRepository, GenderRepository genderRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.genderRepository = genderRepository;
        this.mapper = mapper;
    }


    /**
     * User service check birthdate
     *
     * @param date Birthdate of user
     * @throws InvalidBirthdateException if birthdate is null or if birthdate is invalid
     */
    private void checkBirthDate(LocalDate date, Country country) throws InvalidBirthdateException {
        LocalDate curDate = LocalDate.now();
        LocalDate birth = date;
        int age;

        if (date == null) {
            throw new InvalidBirthdateException("Null parameters are not allowed");
        }
        age = Period.between(birth, curDate).getYears();
        if (age < country.getLegalAge()) {
            throw new InvalidBirthdateException("You must be of legal age");
        }
    }

    /**
     * Check country
     * check if country correspond to an existing gender
     *
     * @param country String containing the country to check
     * @throws InvalidCountryException if country isn't valid (null or doesn't exist)
     * @return return existing country if country is valid
     */
    private Country checkCountry(Country country) throws InvalidCountryException {
        Country exist;

        if (country == null) {
            throw new InvalidCountryException("Null parameters are not allowed");
        }
        exist = this.countryRepository.findByCountryName(country.getCountryName());
        if (exist == null) {
            throw new InvalidCountryException("You must be in France");
        }
        return exist;
    }

    /**
     * Check gender
     * check if gender correspond to an existing gender
     *
     * @param gender String containing the gender to check
     * @throws InvalidGenderException if gender doesn't exist
     * @return return existing gender if gender is valid
     */
    private Gender checkGender(Gender gender) throws InvalidGenderException {
        Gender exist;

        if (gender == null) {
            return null;
        }
        exist = this.genderRepository.findByGenderType(gender.getGenderType());
        if (exist == null) {
            throw new InvalidGenderException("Only male / female / other or empty are allow for gender");
        }
        return exist;
    }

    /**
     * Check username
     * check if username is unique
     *
     * @param name String containing the username to check
     * @throws InvalidUsernameException if username isn't valid
     */
    private void checkName(String name) throws InvalidUsernameException {
        if (name == null) {
            throw new InvalidUsernameException("Null parameters are not allowed");
        }
    }

    /**
     * Check phone number format
     *
     * @param phoneNumber String containing the phone number to check
     * @throws InvalidPhoneException if phone number isn't valid
     */
    private void checkPhoneNumber(String phoneNumber) throws InvalidPhoneException {
        String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3} ?)(\\d{2} ?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher;

        if (phoneNumber == null) {
            return;
        }
        matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new InvalidPhoneException("Invalid phone number");
        }
    }

    /**
     * Create user
     *
     * @param userDTO Contain all user information
     * @return created user
     */
    @Override
    @Supervision(durationMillis = 200)
    public UserDTO createUser(UserDTO userDTO) {
        User user;

        if (userDTO.getId() != null) {
            userDTO.setId(null);
        }
        user = mapper.map(userDTO, User.class);
        checkName(user.getName());
        user.setCountry(checkCountry(user.getCountry()));
        checkBirthDate(user.getBirthdate(), user.getCountry());
        user.setGender(checkGender(user.getGender()));
        checkPhoneNumber(user.getPhoneNumber());
        return mapper.map(this.userRepository.save(user), UserDTO.class);
    }

    /**
     * Get user by user Id
     *
     * @param id User Id
     * @return user
     * @throws UserException if user is invalid
     */
    @Override
    @Supervision(durationMillis = 40)
    public UserDTO getUserById(Long id) throws UserException {
        Optional<User> user = this.userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserException("Invalid UserId");
        }
        return mapper.map(user.get(), UserDTO.class);
    }
}
