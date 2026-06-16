package com.jobportal.Security;

// Spring
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// JWT (io.jsonwebtoken — jjwt library)
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Java
import java.security.Key;
import java.util.Date;
import java.util.UUID;

// Your entity
import com.jobportal.Entities.Users;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long ACCESS_EXPIRY  = 1000L * 60 * 15;         // 15 min
    private static final long REFRESH_EXPIRY = 1000L * 60 * 60 * 24 * 7; // 7 days

    // ── Generate Tokens ──────────────────────────────────────
    public String generateAccessToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("userId", user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRY))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken() {
        // Refresh token is opaque random string — NOT a JWT
        return UUID.randomUUID().toString();
    }

    // ── Validate & Extract ────────────────────────────────────
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
