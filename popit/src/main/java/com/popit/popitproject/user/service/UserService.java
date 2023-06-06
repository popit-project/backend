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

        userRepository.save(newUser);

        UserDTO result = new UserDTO();
        result.setUserId(newUser.getUserId());
        result.setPassword(newUser.getPassword());
        result.setNickname(newUser.getNickname());

        return result;
    }
}
