package com.popit.popitproject.user.controller;

import com.popit.popitproject.user.model.*;
import com.popit.popitproject.user.service.JwtTokenService;
import com.popit.popitproject.user.service.TokenBlacklistService;
import com.popit.popitproject.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public RegistrationResultDTO registerUser(@RequestBody UserDTO userDto) {
        UserDTO registeredUser = userService.registerUser(userDto);
        return new RegistrationResultDTO(registeredUser, "회원가입에 성공하였습니다.");
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
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isLoggedIn = userService.login(loginRequest.getUserId(), loginRequest.getPassword());
        if (isLoggedIn) {
            UserDTO user = userService.getUserInfo(loginRequest.getUserId());
            String token = jwtTokenService.generateUserToken(user.getUserId(), user.getEmail());
            return ResponseEntity.ok("토큰: " + token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    @GetMapping("/info")
    public UserDTO getUserInfo(@RequestParam String userId) {
        return userService.getUserInfo(userId);
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findUserId(@RequestBody FindIdRequest findIdRequest) {
        String userId = userService.findUserIdByEmail(findIdRequest.getEmail());
        if (userId != null) {
            return ResponseEntity.ok("ID: " + userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 이메일로 가입된 계정을 찾을 수 없습니다.");
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
        System.out.println("Received userId: " + changePasswordRequest.getUserId());
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

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok("로그아웃 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
    }
}
