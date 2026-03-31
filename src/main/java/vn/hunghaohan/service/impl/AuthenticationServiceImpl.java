package vn.hunghaohan.service.impl;

import vn.hunghaohan.controller.request.SignInRequest;
import vn.hunghaohan.controller.response.TokenResponse;
import vn.hunghaohan.service.AuthenticationService;

public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public TokenResponse getAccessToken(SignInRequest req) {
        return null;
    }

    @Override
    public TokenResponse getRefreshToken(String req) {
        return null;
    }
}
