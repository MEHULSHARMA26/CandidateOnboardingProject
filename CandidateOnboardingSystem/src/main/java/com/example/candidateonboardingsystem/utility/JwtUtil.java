package com.example.candidateonboardingsystem.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private String secret = "secretKey";  // Keep it safe

    public String generateToken(String username, List<String> roles) {
        logger.info("Generating token for user: {}", username);
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        logger.info("Token generated successfully for user: {}", username);
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = getClaims(token).getSubject();
            logger.info("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Failed to extract username from token: {}", e.getMessage());
            return null;
        }
    }

    public List<String> extractRoles(String token) {
        try {
            List<String> roles = (List<String>) getClaims(token).get("roles");
            logger.info("Extracted roles from token: {}", roles);
            return roles;
        } catch (Exception e) {
            logger.error("Failed to extract roles from token: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            boolean isValid = username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            logger.info("Token validation result for user {}: {}", userDetails.getUsername(), isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaims(token).getExpiration();
            boolean expired = expiration.before(new Date());
            if (expired) {
                logger.warn("Token expired at: {}", expiration);
            }
            return expired;
        } catch (Exception e) {
            logger.error("Failed to check token expiration: {}", e.getMessage());
            return true;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            throw e;
        }
    }

    public String encodeToBase64(byte[] bytes) {
        logger.info("Encoding bytes to Base64");
        String encoded = Base64.getEncoder().encodeToString(bytes);
        logger.info("Encoding successful");
        return encoded;
    }

    public byte[] decodeFromBase64(String encoded) {
        try {
            logger.info("Decoding Base64 string");
            byte[] decoded = Base64.getDecoder().decode(encoded);
            logger.info("Decoding successful");
            return decoded;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to decode Base64 string: {}", e.getMessage());
            return null;
        }
    }

}
