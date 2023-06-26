package com.popit.popitproject.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30분
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public Map<String, Object> generateUserToken(String userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()), SignatureAlgorithm.HS512)
                .compact();

        ZonedDateTime expiryDateUtc = expiryDate.toInstant().atZone(ZoneId.of("UTC"));
        ZonedDateTime expiryDateKst = expiryDateUtc.withZoneSameInstant(ZoneId.of("Asia/Seoul"));

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("token", token);
        tokenData.put("expiresIn", expiryDateKst.toString());

        return tokenData;
    }

    public String generateSellerToken(Long sellerId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(sellerId.toString())
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String refreshSellerToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String email = getEmailFromToken(refreshToken);
            Long sellerId = getSellerIdFromToken(refreshToken);
            return generateSellerToken(sellerId, email);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claims.getBody().get("email", String.class);
    }

    public String getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public Long getSellerIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return Long.parseLong(claims.getBody().getSubject());
    }

    public Authentication getAuthentication(String token) {
        String userId = getUserIdFromToken(token);
        String email = getEmailFromToken(token);

        return new UsernamePasswordAuthenticationToken(userId, email, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public boolean isTokenExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
