package com.popit.popitproject.user.service;

import com.popit.popitproject.user.entity.UserEntity;
import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO registerUser(UserDTO userDto) {
        UserEntity newUser = new UserEntity();
        newUser.setUserId(userDto.getUserId());
        newUser.setPassword(userDto.getPassword());
        newUser.setNickname(userDto.getNickname());
        newUser.setEmail(userDto.getEmail());
        newUser.setPhone(userDto.getPhone());

        userRepository.save(newUser);

        UserDTO result = new UserDTO();
        result.setUserId(newUser.getUserId());
        result.setPassword(newUser.getPassword());
        result.setNickname(newUser.getNickname());
        result.setEmail(newUser.getEmail());
        result.setPhone(newUser.getPhone());

        return result;
    }

    public boolean validateEmail(String userId, String token) {
        // TODO: email validation check
        return true;
    }

    public boolean login(String userId, String password) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            return false;
        }
        return userEntity.getPassword().equals(password);
    }
}