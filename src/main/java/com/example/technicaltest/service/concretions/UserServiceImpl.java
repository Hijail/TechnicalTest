package com.example.technicaltest.service.concretions;

import com.example.technicaltest.exception.InvalidBirthdateException;
import com.example.technicaltest.model.User;
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

    /**
     * User service constructor
     * Init user service repository
     *
     * @param userRepository User repository
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * User service check birthdate
     *
     * @param date Birthdate of user
     * @throws InvalidBirthdateException if birthdate is null or if birthdate is invalid
     */
    private static void checkBirthDate(Date date) throws InvalidBirthdateException {
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

    /**
     * Create user
     *
     * @param user Contain all user information
     * @return created user
     */
    @Override
    public User createUser(User user) throws Exception {
        try {
            checkBirthDate(user.getBirthdate());
        } catch (InvalidBirthdateException e) {
            throw new InvalidBirthdateException(e.getMessage());
        }
        this.userRepository.save(user);
        return user;
    }
}
