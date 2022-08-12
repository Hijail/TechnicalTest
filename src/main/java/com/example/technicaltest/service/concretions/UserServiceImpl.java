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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private void checkBirthDate(Date date) throws InvalidBirthdateException {
        LocalDate curDate = LocalDate.now();
        LocalDate birth;
        int age;

        if (date == null) {
            throw new InvalidBirthdateException("Null parameters are not allowed");
        }
        birth = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
        age = Period.between(birth, curDate).getYears();
        if (age < 18) {
            throw new InvalidBirthdateException("You must be of legal age");
        }
    }

    private void checkCountry(Country country) throws InvalidCountryException {
        Country exist;

        if (country == null) {
            throw new InvalidCountryException("Null parameters are not allowed");
        }
        exist = this.countryRepository.findByCountry(country.getCountry());
        if (exist == null) {
            throw new InvalidCountryException("You must be in France");
        }
    }

    private void checkGender(Gender gender) throws InvalidGenderException {
        Gender exist;

        if (gender == null) {
            return;
        }
        exist = this.genderRepository.findByGender(gender.getGender());
        if (exist == null) {
            throw new InvalidGenderException("Only male / female / other or empty are allow for gender");
        }
    }

    private void checkUsername(String username) throws InvalidUsernameException {
        User exist = this.userRepository.findByUsername(username);

        if (exist != null) {
            throw new InvalidUsernameException("Username already exist");
        }
    }

    private void checkPhoneNumber(String phoneNumber) {
        String patterns = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher;

        if (phoneNumber == null) {
            return;
        }
        matcher = pattern.matcher(phoneNumber);
        if (matcher.matches() != true) {
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
        checkBirthDate(user.getBirthdate());
        checkCountry(user.getCountry());
        checkGender(user.getGender());
        checkPhoneNumber(user.getPhoneNumber());
        this.userRepository.save(user);
        return user;
    }
}
