package vn.hunghaohan.service;

import vn.hunghaohan.controller.request.SignInRequest;
import vn.hunghaohan.controller.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest req);
    TokenResponse getRefreshToken(String req);
}
