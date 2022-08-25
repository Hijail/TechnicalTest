package com.example.technicaltest.service.abstractions;

import com.example.technicaltest.dto.UserDTO;

public interface IUserService {
    UserDTO createUser(UserDTO user);

    UserDTO getUserById(Long id);
}
