package com.example.GftApplication.configs.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJWT(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getDocument()))
                .claim("userId", userPrincipal.getId())
                .claim("userName", userPrincipal.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getEmailJWT(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJWT(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();

            return true;
        } catch (SignatureException signatureException) {
            throw new RuntimeException("Invalid JWT signature", signatureException);
        } catch (MalformedJwtException malformedJwtException) {
            throw new RuntimeException("Invalid JWT token", malformedJwtException);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RuntimeException("JWT token is expired", expiredJwtException);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new RuntimeException("JWT token is unsupported", unsupportedJwtException);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new RuntimeException("JWT claims string is empty", illegalArgumentException);
        }
    }
}