package com.example.ecommerce.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";
    private final long EXPIRATION = 1000*60*60;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role",userDetails.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration((new Date(System.currentTimeMillis() + EXPIRATION)))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }
    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
