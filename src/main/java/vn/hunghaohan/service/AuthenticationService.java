package vn.hunghaohan.service;

import vn.hunghaohan.controller.request.SignInRequest;
import vn.hunghaohan.controller.response.TokenResponse;

import java.nio.file.AccessDeniedException;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest req) throws AccessDeniedException;
    TokenResponse getRefreshToken(String req);
}
