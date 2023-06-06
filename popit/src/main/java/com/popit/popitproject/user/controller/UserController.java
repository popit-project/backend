package com.popit.popitproject.user.controller;

import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody UserDTO userDto) {
        return userService.registerUser(userDto);
    }
}