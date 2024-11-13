package com.mojkvart.rest;

import com.mojkvart.model.TokenDTO;
import com.mojkvart.service.JwtService;
import com.mojkvart.util.TokenResponse;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/tokens")
@RequiredArgsConstructor
public class TokenResource {

    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<TokenResponse> extractInfoFromToken(@RequestBody @Valid TokenDTO tokenDTO) {
        String email = jwtService.getEmailFromToken(tokenDTO.getToken());
        Claims claims = jwtService.getAllClaims(tokenDTO.getToken());
        String role = (String) claims.get("role");
        TokenResponse tokenResponse = new TokenResponse(email, role);
        return ResponseEntity.ok(tokenResponse);
    }
}