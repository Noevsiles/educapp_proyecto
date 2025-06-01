package com.example.educapp_proyecto.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "MiClaveSuperLargaYSeguraParaJWTHS512_educapp2025_secretaClave1234567890";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_MS = 86400000; // 1 dia

    public String generarToken(String email, String nombre, String rol) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(email)
                .claim("nombre", nombre)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extraerEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extraerEmailDesdeRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // Quita el prefijo "Bearer "
            return extraerEmail(token);
        }
        return null;
    }

}
