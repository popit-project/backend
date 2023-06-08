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

    @GetMapping("/validate-email")
    public String validateEmail(@RequestParam String userId, @RequestParam String code) {
        boolean isVerified = userService.validateEmail(userId, code);
        if (isVerified) {
            return "인증이 완료 되었습니다.";
        } else {
            return "인증에 실패 하였습니다. 다시 시도해 주세요.";
        }
    }

    @PostMapping("/login")
    public boolean loginUser(@RequestBody UserDTO userDto) {
        return userService.login(userDto.getUserId(), userDto.getPassword());
    }
}
