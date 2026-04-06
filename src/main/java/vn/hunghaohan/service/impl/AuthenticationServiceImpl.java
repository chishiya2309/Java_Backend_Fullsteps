package vn.hunghaohan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.hunghaohan.common.TokenType;
import vn.hunghaohan.controller.request.SignInRequest;
import vn.hunghaohan.controller.response.TokenResponse;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.AuthenticationService;
import vn.hunghaohan.service.JwtService;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInRequest req) throws AccessDeniedException {
        log.info("Get access token");

        List<String> authorities = new ArrayList<>();
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            log.info("isAuthenticated = {}", authenticate.isAuthenticated());
            log.info("Authorities: {}", authenticate.getAuthorities());
            authenticate.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));

            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }catch(BadCredentialsException | DisabledException e) {
            log.error("Login fail, message= {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        String accessToken = jwtService.generateAccessToken(req.getUsername(), authorities);
        String refreshToken = jwtService.generateRefreshToken(req.getUsername(), authorities);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String req) throws AccessDeniedException {
        log.info("Refresh access token");

        try {
            // Extract username from refresh token
            String username = jwtService.extractUsername(req, TokenType.REFRESH_TOKEN);

            // Get user information
            var user = userRepository.findByUserName(username);
            List<String> authorities = new ArrayList<>();
            user.getAuthorities().forEach(auth -> authorities.add(auth.getAuthority()));

            // Generate new access token
            String newAccessToken = jwtService.generateAccessToken(username, authorities);

            return TokenResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(req)
                    .build();
        } catch (AccessDeniedException e) {
            log.error("Refresh token fail, message= {}", e.getMessage());
            throw e;
        }
    }
}
