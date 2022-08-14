package com.example.technicaltest.controller;

import com.example.technicaltest.model.User;
import com.example.technicaltest.service.abstractions.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
    public  static final String BASE_URL = "/api/v1/users";

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User createUser(@RequestBody User user)
    {
        return new User();
    }
}
