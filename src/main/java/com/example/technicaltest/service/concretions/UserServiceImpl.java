package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.InvalidBirthdateException;
import com.example.technicaltest.exception.InvalidCountryException;
import com.example.technicaltest.model.Country;
import com.example.technicaltest.model.User;
import com.example.technicaltest.repository.CountryRepository;
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

    /**
     * User service constructor
     * Init user service repository
     *
     * @param userRepository User repository
     */
    public UserServiceImpl(UserRepository userRepository, CountryRepository countryRepository) {
        this.userRepository = userRepository;
        this.countryRepository = countryRepository;
    }

    /**
     * User service check birthdate
     *
     * @param date Birthdate of user
     * @throws InvalidBirthdateException if birthdate is null or if birthdate is invalid
     */
    private void checkBirthDate(Date date) throws InvalidBirthdateException {
        LocalDate curDate = LocalDate.now();
        LocalDate birth = null;
        Integer age = 0;

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

    /**
     * Create user
     *
     * @param user Contain all user information
     * @return created user
     */
    @Override
    public User createUser(User user) throws Exception {
        checkBirthDate(user.getBirthdate());
        checkCountry(user.getCountry());
        this.userRepository.save(user);
        return user;
    }
}
