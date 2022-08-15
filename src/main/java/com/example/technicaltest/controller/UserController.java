package com.example.technicaltest.controller;

import com.example.technicaltest.exception.UserException;
import com.example.technicaltest.model.User;
import com.example.technicaltest.response.ResponseHandler;
import com.example.technicaltest.service.abstractions.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Class Rest API User Controller
 * @author Baptiste Gellato
 * @version 0.0.1
 */
@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
    public  static final String BASE_URL = "/api/v1/users";

    private final IUserService userService;

    /**
     * User Controller constructor
     * Init User controller service
     * @param userService  User Service
     */
    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * Create user
     *
     * @param user  Request body how contain user information
     * @return created user or Bad Request if fail
     */
    @ApiOperation(value = "createUser", notes = "create new user", nickname = "createUser", code = 201)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 201, message = "User Created",
                    response = User.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "[\"Null parameters are not allowed\", \"You must be of legal age\", \"You must be in France\", \"Only male / female / other or empty are allow for gender\", \"Username already exist\", \"Invalid phone number\"]"),
    })
    @PostMapping()
    public ResponseEntity<Object> createUser(@RequestBody User user)
    {
        User newUser;

        try {
            newUser = this.userService.createUser(user);
        } catch (Exception exc) {
            return ResponseHandler.generateResponse(exc.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
        return ResponseHandler.generateResponse("User Created", HttpStatus.CREATED, newUser);
    }

    /**
     * Get user by user Id
     *
     * @param id  User Id
     * @return user if success or Bad Request if fail
     */
    @ApiOperation(value = "getUserById", notes = "get user by id", nickname = "getUserById")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Service not found"),
            @ApiResponse(code = 200, message = "",
                    response = User.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Invalid UserId")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserById(@ApiParam(value = "testId", required = true, defaultValue = "5")
                                                  @PathVariable long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseHandler.generateResponse("", HttpStatus.OK, user);
        }
        catch (UserException err) {
            return ResponseHandler.generateResponse(err.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}
