package com.uca.telemedicina.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secretKey;
    @Value("${jwt.expiration}") private String expirationTime;
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + Long.parseLong(expirationTime));
        return Jwts.builder().subject(username).issuedAt(now).expiration(expiry).signWith(getKey()).compact();
    }
    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean validateToken(String token) {
        try { Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token); return true; }
        catch (Exception e) { return false; }
    }
}
