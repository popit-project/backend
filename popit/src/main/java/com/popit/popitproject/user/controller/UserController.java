package com.popit.popitproject.user.controller;

import com.popit.popitproject.user.model.*;
import com.popit.popitproject.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


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

    // 변경필요 사항222
    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isLoggedIn = userService.login(loginRequest.getUserId(), loginRequest.getPassword());
        if (isLoggedIn) {
            return "로그인에 성공하였습니다.";
        } else {
            return "로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.";
        }
    }

    @GetMapping("/info")
    public UserDTO getUserInfo(@RequestParam String userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/find-id")
    public String findUserId(@RequestBody FindIdRequest findIdRequest) {
        String userId = userService.findUserIdByEmail(findIdRequest.getEmail());
        if (userId != null) {
            return "ID: " + userId;
        } else {
            return "해당 이메일로 가입된 계정을 찾을 수 없습니다.";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        boolean isReset = userService.resetPasswordByEmail(resetPasswordRequest.getEmail());
        if (isReset) {
            return "새로운 비밀번호가 이메일로 전송되었습니다.";
        } else {
            return "비밀번호 재설정에 실패했습니다. 다시 시도해 주세요.";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        boolean isChanged = userService.changePassword(
                changePasswordRequest.getUserId(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword());
        if (isChanged) {
            return "비밀번호가 성공적으로 변경되었습니다.";
        } else {
            return "비밀번호 변경에 실패했습니다. 기존 비밀번호를 확인해주세요.";
        }
    }
}
