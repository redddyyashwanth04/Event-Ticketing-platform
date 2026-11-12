package com.example.Ticketing_project.security;



import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // Registers this class as a Spring component so we can inject it anywhere
public class JwtUtils {

    // 1. @Value annotation reads configuration variables from application.properties.
    // If 'app.jwt.secret' is missing, it falls back to the long secure default string provided after the colon.
    @Value("${app.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}") // 24 hours in milliseconds (24 * 60 * 60 * 1000)
    private int jwtExpirationMs;

    // 2. Helper method to convert our secret text string into a cryptographic HMAC-SHA key object
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // 3. GENERATE TOKEN METHOD
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Storing the user's email as the "subject" inside the token payload
                .setIssuedAt(new Date()) // Current timestamp when token is created
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Expiration timestamp (Now + 24 hours)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Cryptographically sign it using HMAC-SHA256 and our secret key
                .compact(); // Build and serialize the token into a compact URL-safe string format
    }

    // 4. EXTRACT EMAIL METHOD
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // Provide the secret key to verify the signature
                .build()
                .parseClaimsJws(token) // Parses and validates the token structure and signature
                .getBody()
                .getSubject(); // Retrieves the email we stored inside the subject field earlier
    }

    // 5. VALIDATE TOKEN METHOD
    public boolean validateToken(String token) {
        try {
            // If the token is tampered with, expired, or malformed, parseClaimsJws() throws an exception
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true; // Token is completely valid!
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token is invalid or expired
        }
    }
}
