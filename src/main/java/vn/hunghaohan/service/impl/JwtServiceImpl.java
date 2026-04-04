package vn.hunghaohan.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import vn.hunghaohan.common.TokenType;
import vn.hunghaohan.exception.InvalidDataException;
import vn.hunghaohan.service.JwtService;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static vn.hunghaohan.common.TokenType.ACCESS_TOKEN;
import static vn.hunghaohan.common.TokenType.REFRESH_TOKEN;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryMinutes}")
    private long expiryMinutes;

    @Value("${jwt.expiryDays}")
    private long expiryDays;

    @Value("${jwt.accessKey}")
    private String accessKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;



    @Override
    public String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generating access token for user {} with authorities={}", userId, authorities);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", String.valueOf(userId));
        claims.put("role", authorities == null ? List.of() : authorities.stream().map(GrantedAuthority::getAuthority).toList());

        return generateAccessToken(claims, username);
    }

    @Override
    public String generateRefreshToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        log.info("Generating refresh token for user {} with authorities={}", userId, authorities);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", String.valueOf(userId));
        claims.put("role", authorities == null ? List.of() : authorities.stream().map(GrantedAuthority::getAuthority).toList());

        return generateRefreshToken(claims, username);
    }

    @Override
    public String extractUsername(String token, TokenType type) throws AccessDeniedException {
        log.info("Extract username from token {} with type = {}", token, type);
        return extractClaims(type, token, Claims::getSubject);
    }

    private <T> T extractClaims(TokenType type, String token, Function<Claims, T> claimsExtractor) throws AccessDeniedException {
        final Claims claims = extractAllClaim(token, type);
        return claimsExtractor.apply(claims);
    }

    private Claims extractAllClaim(String token, TokenType type) throws AccessDeniedException {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey(type))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new AccessDeniedException("Access denied!, token expired: " + e.getMessage());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AccessDeniedException("Access denied!, error: " + e.getMessage());
        }
    }

    private String generateAccessToken(Map<String, Object> claims, String username) {
        log.info("Generate access token for username {} with claims={}", username, claims);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiryMinutes))
                .signWith(getSigningKey(ACCESS_TOKEN))
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, String username) {
        log.info("Generate refresh token for username {} with claims={}", username, claims);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryDays))
                .signWith(getSigningKey(REFRESH_TOKEN))
                .compact();
    }

    private SecretKey getSigningKey(TokenType type) {
        switch(type) {
            case ACCESS_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
            }
            case REFRESH_TOKEN -> {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
            }
            default -> throw new InvalidDataException("Invalid token type");
        }
    }
}
