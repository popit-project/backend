package com.popit.popitproject.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.user.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDTO user;

    @BeforeEach
    public void setUp() {
        user = new UserDTO();
        user.setUserId("testUser");
        user.setPassword("testPassword");
        user.setNickname("testNickname");
        user.setEmail("testEmail@popit.com");
        user.setPhone("010-1234-5678");
    }

    @Test
    @DisplayName("회원가입 Test")
    public void testRegisterUser() throws Exception {
        when(userService.registerUser(user)).thenReturn(user);

        mockMvc.perform(post("/user/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 인증 Test")
    public void testValidateEmail() throws Exception {
        when(userService.validateEmail(user.getUserId(), "123456")).thenReturn(true);

        mockMvc.perform(get("/user/validate-email")
                        .param("userId", user.getUserId())
                        .param("code", "123456"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 Test")
    public void testLoginUser() throws Exception {
        when(userService.login(user.getUserId(), user.getPassword())).thenReturn(true);

        mockMvc.perform(post("/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
}
