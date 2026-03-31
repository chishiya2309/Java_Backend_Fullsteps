package vn.hunghaohan.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import vn.hunghaohan.common.TokenType;
import vn.hunghaohan.service.JwtService;

import java.util.Collection;

@Service
@Slf4j(topic = "JWT-SERVICE")
public class JwtServiceImpl implements JwtService {
    @Override
    public String generateAccessToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {

        return "";
    }

    @Override
    public String generateRefreshToken(long userId, String username, Collection<? extends GrantedAuthority> authorities) {
        return "";
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return "";
    }
}
