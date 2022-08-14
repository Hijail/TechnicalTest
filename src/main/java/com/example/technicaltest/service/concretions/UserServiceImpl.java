package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.*;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.Gender;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
import com.example.technicaltest.repository.GenderRepository;
import com.example.technicaltest.repository.UserRepository;
import com.example.technicaltest.service.abstractions.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
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

    /**
     * User service constructor
     * Init user service repository
     *
     * @param userRepository User repository
     * @param countryRepository Country repository
     * @param genderRepository Gender repository
     */
    public UserServiceImpl(UserRepository userRepository, CountryRepository countryRepository, GenderRepository genderRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
        this.genderRepository = genderRepository;
    }

    /**
     * User service check birthdate
     *
     * @param date Birthdate of user
     * @throws InvalidBirthdateException if birthdate is null or if birthdate is invalid
     */
    private void checkBirthDate(Date date, Country country) throws InvalidBirthdateException {
        LocalDate curDate = LocalDate.now();
        LocalDate birth;
        int age;

        if (date == null) {
            throw new InvalidBirthdateException("Null parameters are not allowed");
        }
        birth = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
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
        exist = this.countryRepository.findByCountry(country.getCountry());
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
        exist = this.genderRepository.findByGender(gender.getGender());
        if (exist == null) {
            throw new InvalidGenderException("Only male / female / other or empty are allow for gender");
        }
        return exist;
    }

    /**
     * Check username
     * check if username is unique
     *
     * @param username String containing the username to check
     * @throws InvalidUsernameException if username isn't valid
     */
    private void checkUsername(String username) throws InvalidUsernameException {
        User exist = this.userRepository.findByUsername(username);

        if (exist != null) {
            throw new InvalidUsernameException("Username already exist");
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
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
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
     * @param user Contain all user information
     * @return created user
     */
    @Override
    public User createUser(User user) {
        checkUsername(user.getUsername());
        user.setCountry(checkCountry(user.getCountry()));
        checkBirthDate(user.getBirthdate(), user.getCountry());
        user.setGender(checkGender(user.getGender()));
        checkPhoneNumber(user.getPhoneNumber());
        this.userRepository.save(user);
        return user;
    }

    /**
     * Get user by user Id
     *
     * @param id User Id
     * @return user
     * @throws UserException if user is invalid
     */
    @Override
    public User getUserById(Long id) throws UserException {
        Optional<User> user = this.userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserException("Invalid UserId");
        }
        return user.get();
    }
}
