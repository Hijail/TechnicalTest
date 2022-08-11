package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.InvalidBirthdateException;
import com.example.technicaltest.exception.InvalidCountryException;
import com.example.technicaltest.exception.InvalidGenderException;
import com.example.technicaltest.exception.InvalidUsernameException;
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
        this.userRepository.save(user);
        return user;
    }
}
