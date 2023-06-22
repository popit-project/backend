package com.popit.popitproject.user.controller;

import com.popit.popitproject.user.model.GoogleInfResponse;
import com.popit.popitproject.user.model.GoogleRequest;
import com.popit.popitproject.user.model.GoogleResponse;
import com.popit.popitproject.user.model.UserDTO;
import com.popit.popitproject.user.service.JwtTokenService;
import com.popit.popitproject.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
                + "&redirect_uri=http://localhost:8082/api/login/google&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }

    @RequestMapping(value = "/api/login/google", method = RequestMethod.GET)
    public ResponseEntity<?> loginGoogle(@RequestParam(value = "code") String authCode) {
        RestTemplate restTemplate = new RestTemplate();
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientPw)
                .code(authCode)
                .redirectUri("http://localhost:8082/api/login/google")
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

        String token = jwtTokenService.generateUserToken(userDTO.getUserId(), userDTO.getEmail());
        return ResponseEntity.ok(token);
    }
}
