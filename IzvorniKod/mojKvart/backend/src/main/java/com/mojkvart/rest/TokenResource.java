package com.mojkvart.rest;

import com.mojkvart.model.TokenDTO;
import com.mojkvart.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tokens")
public class TokenResource {

    @PostMapping
    public ResponseEntity<Boolean> tokenValidity(@RequestBody TokenDTO tokenDTO) {
        Boolean tf = JwtUtil.isTokenValid(tokenDTO.getToken());
        return ResponseEntity.ok(tf);
    }
}
