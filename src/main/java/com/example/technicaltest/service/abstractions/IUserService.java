package com.example.technicaltest.service.abstractions;

import com.example.technicaltest.model.User;

public interface IUserService {
    User createUser(User user) throws Exception;
}
