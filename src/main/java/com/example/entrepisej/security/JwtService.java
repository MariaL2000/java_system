package com.example.entrepisej.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    public String getToken(UserDetails user) {
        Map<String, Object> extraClaims = new HashMap<>();
        
        var roles = user.getAuthorities().stream()
                .map(authority -> {
                    String roleName = authority.getAuthority();
                    if (roleName.startsWith("ROLE_ROLE_")) {
                        return roleName.substring(5);
                    }
                    if (!roleName.startsWith("ROLE_")) {
                        return "ROLE_" + roleName;
                    }
                    return roleName;
                })
                .collect(Collectors.toList());
    
        extraClaims.put("roles", roles);
    
        return getToken(extraClaims, user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        // En la versión 0.12.x se usa claims() en lugar de setClaims()
        // y verifyWith() para la firma
        return Jwts.builder()
            .claims(extraClaims)
            .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
            .signWith(getKey()) // Ya no necesita SignatureAlgorithm.HS256 explícito
            .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        // CAMBIO CLAVE: parserBuilder() -> parser(), setSigningKey() -> verifyWith()
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token) // En 0.12.x se usa parseSignedClaims
            .getPayload(); // getBody() -> getPayload()
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }
}