package com.example.technicaltest.service.concretions;

import com.example.technicaltest.model.User;
import com.example.technicaltest.service.abstractions.IUserService;

import java.util.Date;

public class UserServiceImpl implements IUserService {



    private static boolean checkBirthDate(Date date) {
        return false;
    }

    @Override
    public User createUser(User user) {
        return null;
    }
}
