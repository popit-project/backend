package com.popit.popitproject.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popit.popitproject.user.model.ChangePasswordRequest;
import com.popit.popitproject.user.model.FindIdRequest;
import com.popit.popitproject.user.model.ResetPasswordRequest;
import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.service.UserService;
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

    @Test
    @DisplayName("내 정보 보기 Test")
    public void testGetUserInfo() throws Exception {
        when(userService.getUserInfo(user.getUserId())).thenReturn(user);

        mockMvc.perform(get("/user/info")
                        .param("userId", user.getUserId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("ID 찾기 Test")
    public void testFindUserId() throws Exception {
        FindIdRequest findIdRequest = new FindIdRequest();
        findIdRequest.setEmail("testEmail@popit.com");

        when(userService.findUserIdByEmail(findIdRequest.getEmail())).thenReturn(user.getUserId());

        mockMvc.perform(post("/user/find-id")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(findIdRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 재설정 Test")
    public void testResetPassword() throws Exception {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setEmail("testEmail@popit.com");

        when(userService.resetPasswordByEmail(resetPasswordRequest.getEmail())).thenReturn(true);

        mockMvc.perform(post("/user/reset-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경 Test")
    public void testChangePassword() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUserId("testUser");
        changePasswordRequest.setOldPassword("testPassword");
        changePasswordRequest.setNewPassword("newTestPassword");

        when(userService.changePassword(changePasswordRequest.getUserId(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword())).thenReturn(true);

        mockMvc.perform(post("/user/change-password")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk());
    }
}