package vn.hunghaohan.service;

import vn.hunghaohan.common.TokenType;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String username, List<String> authorities);
    String generateRefreshToken(String username, List<String> authorities);
    String extractUsername(String token, TokenType type) throws AccessDeniedException;
}
