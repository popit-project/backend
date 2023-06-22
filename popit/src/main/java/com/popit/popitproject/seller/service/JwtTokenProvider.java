package com.popit.popitproject.seller.service;


import com.popit.popitproject.seller.CustomSellerDetailsService;
import com.popit.popitproject.seller.SellerEntity;
import com.popit.popitproject.seller.SellerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@SuppressWarnings("ALL")
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final SellerEntity newUser;
    private final CustomSellerDetailsService customSellerDetailsService;
    private final SellerRepository sellerRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateSellerToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS512))
            .compact();
    }


    public String generateUserToken(String email) {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);

        SellerEntity newUser = (SellerEntity) customSellerDetailsService.loadUserByUsername(email);
        newUser.setToken(String.valueOf(token));

        return String.valueOf(token);
    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 접두사 제거
        }
        return null; // 토큰이 발견되지 않거나 잘못된 형식일 경우 null 반환
    }

    public boolean validateUserToken(String token) {
        SellerEntity user = sellerRepository.findByToken(token)
            .orElseThrow(() -> new UsernameNotFoundException("토큰에 일치하는 유저가 없습니다."));
        return user != null;
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getExtractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        return null; // Invalid authorization header or missing prefix
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }
}