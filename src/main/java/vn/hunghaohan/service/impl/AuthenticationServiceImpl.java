package vn.hunghaohan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.hunghaohan.common.TokenType;
import vn.hunghaohan.controller.request.SignInRequest;
import vn.hunghaohan.controller.response.TokenResponse;
import vn.hunghaohan.exception.ResourceNotFoundException;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.AuthenticationService;
import vn.hunghaohan.service.JwtService;

import java.nio.file.AccessDeniedException;

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

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(AuthenticationException e) {
            log.error("Login fail, message= {}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        var user = userRepository.findByUserName(req.getUsername());

        String accessToken = jwtService.generateAccessToken(user.getId(), req.getUsername(), user.getAuthorities());
        String refreshToken = jwtService.generateRefreshToken(user.getId(), req.getUsername(), user.getAuthorities());

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
            if (user == null) {
                throw new ResourceNotFoundException("User not found");
            }

            // Generate new access token
            String newAccessToken = jwtService.generateAccessToken(user.getId(), username, user.getAuthorities());

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
