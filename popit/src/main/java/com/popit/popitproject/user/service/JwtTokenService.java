package com.popit.popitproject.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Service
public class JwtTokenService {

    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30분
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String generateUserToken(String userId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(userId)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateSellerToken(String sellerId, String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(sellerId)
                .claim("email", email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()), SignatureAlgorithm.HS512)
                .compact();
    }

    public String refreshSellerToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String email = getEmailFromToken(refreshToken);
            String sellerId = getSellerIdFromToken(refreshToken);
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

    private String getEmailFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claims.getBody().get("email", String.class);
    }

    public String getUserIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public String getSellerIdFromToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        return claims.getBody().getSubject();
    }

    public Authentication getAuthentication(String token) {
        String userId = getSellerIdFromToken(token);
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
