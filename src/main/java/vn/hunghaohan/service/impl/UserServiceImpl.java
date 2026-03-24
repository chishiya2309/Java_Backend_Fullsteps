package vn.hunghaohan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import vn.hunghaohan.controller.request.user.UserCreationRequest;
import vn.hunghaohan.controller.request.user.UserPasswordRequest;
import vn.hunghaohan.controller.request.user.UserUpdateRequest;
import vn.hunghaohan.controller.response.UserResponse;
import vn.hunghaohan.service.UserService;

import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Override
    public List<UserResponse> findAll() {
        return List.of();
    }

    @Override
    public UserResponse findById(Long id) {
        return null;
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    public int save(UserCreationRequest req) {
        return 0;
    }

    @Override
    public int update(UserUpdateRequest req) {
        return 0;
    }

    @Override
    public void changePassword(UserPasswordRequest req) {

    }

    @Override
    public void delete(Long id) {

    }
}
