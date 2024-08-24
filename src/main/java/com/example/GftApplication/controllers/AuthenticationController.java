package com.example.GftApplication.controllers;

import com.example.GftApplication.configs.security.JwtProvider;
import com.example.GftApplication.dtos.Jwt.JwtDTO;
import com.example.GftApplication.dtos.Login.LoginDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/login")
public class AuthenticationController {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<JwtDTO> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getDocument(), loginDTO.getPassword())
            );

            String jwt = jwtProvider.generateJWT(authentication);
            return ResponseEntity.status(HttpStatus.OK).body(new JwtDTO(jwt));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JwtDTO("Authentication error: " + ex.getMessage()));
        }
    }
}