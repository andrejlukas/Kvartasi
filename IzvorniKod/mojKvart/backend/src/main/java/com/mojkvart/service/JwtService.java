package com.mojkvart.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {

    private static final String SECRET_KEY = "KvartasiDugackiTajniKljucKojiJeMinimalnoDuljine32Znaka";

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(getAllClaims(token));
    }

    public String getEmailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateToken(Map<String, Object> allClaims, String email) {
        Date now = new Date(System.currentTimeMillis());
        Date then = new Date(System.currentTimeMillis() + 30 * 60 * 1000); // 30 minuta
        return Jwts
                .builder()
                .setClaims(allClaims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(then)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String email = getEmailFromToken(token);

        return email.equals(userDetails.getUsername()) && getClaim(token, Claims::getExpiration).after(new Date());
    }
}