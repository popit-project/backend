package com.popit.popitproject.user.service;

import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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

        if (!userDto.getPassword().equals(userDto.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        UserEntity newUser = new UserEntity();
        newUser.setUserId(userDto.getUserId());
        newUser.setPassword(userDto.getPassword());
        newUser.setEmail(userDto.getEmail());
        newUser.setPhone(userDto.getPhone());
        newUser.setNickname(userDto.getNickname());
        newUser.getRoles().add(UserEntity.Role.ROLE_USER);

        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        newUser.setToken(String.valueOf(token));

        userRepository.save(newUser);

        emailService.sendEmail(newUser.getEmail(), "POPIT-이메일 인증 요청",
            "인증번호 6자리를 입력해 주세요: " + token);

        UserDTO result = new UserDTO();
        result.setUserId(newUser.getUserId());
        result.setPassword(newUser.getPassword());
        result.setEmail(newUser.getEmail());
        result.setPhone(newUser.getPhone());
        result.setNickname(newUser.getNickname());

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

//    public boolean login(String userId, String password) {
//        List<UserEntity> userEntities = userRepository.findByUserId(userId);
//        if (userEntities.isEmpty()) {
//            return false;
//        }
//        if (userEntities.size() > 1) {
//        }
//        UserEntity userEntity = userEntities.get(0);
//        return userEntity.getPassword().equals(password);
//    }

    public UserDTO getUserInfo(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new IllegalArgumentException("가입 된 회원이 아닙니다.");
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPhone(userEntity.getPhone());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setSellerModeButton(userEntity.getSellerModeButton());
        userDTO.setSellerId(userEntity.getSellerId());
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

    public UserDTO findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(userEntity.getUserId());
            userDTO.setEmail(userEntity.getEmail());
            return userDTO;
        }
        return null;
    }

    public UserDTO registerGoogleUser(String email) {
        UserEntity existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            UserDTO existingUserDTO = new UserDTO();
            existingUserDTO.setEmail(existingUser.getEmail());
            existingUserDTO.setUserId(existingUser.getUserId());
            return existingUserDTO;
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.getRoles().add(UserEntity.Role.ROLE_USER);

        newUser.setUserId(generateRandomAlphanumericString(8)); // 임시 user ID 생성
        newUser.setPassword(generateRandomAlphanumericString(8)); // 임시 비밀번호 생성
        newUser.setPhone("000-0000-0000"); // 임시 전화번호 설정
        newUser.setNickname("googleNick");

        userRepository.save(newUser);

        UserDTO result = new UserDTO();
        result.setEmail(newUser.getEmail());
        result.setUserId(newUser.getUserId());

        return result;
    }


    public void updateLastTokenUsed(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            userEntity.setLastTokenUsed(new Date());
            userRepository.save(userEntity);
        }
    }

    public boolean changeUserInfo(String email, String newNickname) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            userEntity.setNickname(newNickname);
            userRepository.save(userEntity);
            return true;
        }
        return false;
    }
}