package com.popit.popitproject.user.service;

import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public UserDTO registerUser(UserDTO userDto) {
        UserEntity existingUser = userRepository.findByUserId(userDto.getUserId());
        if (existingUser != null) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        UserEntity existingEmail = userRepository.findByEmail(userDto.getEmail());
        if (existingEmail != null) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUserId(userDto.getUserId());
        newUser.setPassword(userDto.getPassword());
        newUser.setNickname(userDto.getNickname());
        newUser.setEmail(userDto.getEmail());
        newUser.setPhone(userDto.getPhone());

        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        newUser.setToken(String.valueOf(token));

        userRepository.save(newUser);

        emailService.sendEmail(newUser.getEmail(), "POPIT-이메일 인증 요청",
                "인증번호 6자리를 입력해 주세요: " + token);

        UserDTO result = new UserDTO();
        result.setUserId(newUser.getUserId());
        result.setPassword(newUser.getPassword());
        result.setNickname(newUser.getNickname());
        result.setEmail(newUser.getEmail());
        result.setPhone(newUser.getPhone());

        return result;
    }

    public boolean validateEmail(String userId, String token) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return false;
        }
        return userEntity.getToken().equals(token);
    }

    public boolean login(String userId, String password) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return false;
        }
        return userEntity.getPassword().equals(password);
    }

    public UserDTO getUserInfo(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new IllegalArgumentException("가입 된 회원이 아닙니다.");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPhone(userEntity.getPhone());

        return userDTO;
    }

    public String findUserIdByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            return userEntity.getUserId();
        }
        return null;
    }

    public boolean resetPasswordByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            String newPassword = generateRandomAlphanumericString(8);
            userEntity.setPassword(newPassword);
            userRepository.save(userEntity);
            emailService.sendEmail(userEntity.getEmail(), "POPIT-비밀번호 재설정",
                    "새로운 비밀번호는 " + newPassword + " 입니다.");
            return true;
        }
        return false;
    }

    private String generateRandomAlphanumericString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        System.out.println("UserId: " + userId);
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity != null && userEntity.getPassword().equals(oldPassword)) {
            userEntity.setPassword(newPassword);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }
}
