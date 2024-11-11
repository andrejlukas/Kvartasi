package com.mojkvart.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class jwtUtil {

    // Tajni ključ - ovo treba sklonit negdje
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("SuperSecretKeyThatIsAtLeast32Chars".getBytes());

    // Generiranje JWT tokena
    public static String generateToken(String email, String role, Integer id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 1 sat trajanja tokena

        // Kreiranje tokena
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
