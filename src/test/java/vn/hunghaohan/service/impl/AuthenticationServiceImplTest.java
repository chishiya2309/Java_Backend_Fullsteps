package vn.hunghaohan.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import vn.hunghaohan.controller.response.TokenResponse;
import vn.hunghaohan.model.UserEntity;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.JwtService;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private org.springframework.security.authentication.AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationServiceImpl(userRepository, authenticationManager, jwtService);
    }

    @Test
    void getRefreshToken_shouldReturnNewAccessTokenAndKeepRefreshToken() throws AccessDeniedException {
        String refreshToken = "refresh-token-value";
        String username = "john.doe@example.com";
        java.util.Collection<? extends GrantedAuthority> authorities = java.util.List.of();

        UserEntity user = org.mockito.Mockito.mock(UserEntity.class);
        when(user.getId()).thenReturn(10L);

        when(jwtService.extractUsername(refreshToken, vn.hunghaohan.common.TokenType.REFRESH_TOKEN)).thenReturn(username);
        when(userRepository.findByUserName(username)).thenReturn(user);
        when(jwtService.generateAccessToken(10L, username, authorities)).thenReturn("new-access-token");

        TokenResponse response = authenticationService.getRefreshToken(refreshToken);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }
}


