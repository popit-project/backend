package com.popit.popitproject.user.controller;

import com.popit.popitproject.user.model.*;
import com.popit.popitproject.user.service.JwtTokenService;
import com.popit.popitproject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class GoogleLoginController {
    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.pw}")
    private String googleClientPw;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @RequestMapping(value = "/api/login/google", method = RequestMethod.POST)
    public String loginUrlGoogle() {
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=http://localhost:5173&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }

    @RequestMapping(value = "/api/login/google", method = RequestMethod.GET)
    public void loginGoogle(@RequestParam(value = "code") String authCode, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri("http://localhost:5173")
                .grantType("authorization_code").build();

        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);
        String jwtToken = resultEntity.getBody().getId_token();

        Map<String, String> map = new HashMap<>();
        map.put("id_token", jwtToken);
        ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfResponse.class);
        String email = resultEntity2.getBody().getEmail();

        UserDTO userDTO = userService.findByEmail(email);
        if (userDTO == null) {
            userDTO = userService.registerGoogleUser(email);
        }

        Map<String, Object> tokenData = jwtTokenService.generateUserToken(userDTO.getUserId(), userDTO.getEmail());
        userService.updateLastTokenUsed(userDTO.getEmail());

        String token = (String) tokenData.get("token");

        response.sendRedirect("http://localhost:5173?token=" + token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isLoggedIn = userService.login(loginRequest.getUserId(), loginRequest.getPassword());
        if (isLoggedIn) {
            UserDTO user = userService.getUserInfo(loginRequest.getUserId());
            Map<String, Object> tokenData = jwtTokenService.generateUserToken(user.getUserId(), user.getEmail());
            userService.updateLastTokenUsed(user.getEmail());
            return ResponseEntity.ok(tokenData);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요.");
        }
    }
}
