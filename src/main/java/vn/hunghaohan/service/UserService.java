package vn.hunghaohan.service;

import vn.hunghaohan.controller.request.user.UserCreationRequest;
import vn.hunghaohan.controller.request.user.UserPasswordRequest;
import vn.hunghaohan.controller.request.user.UserUpdateRequest;
import vn.hunghaohan.controller.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse findByUsername(String username);
    int save(UserCreationRequest req);
    int update(UserUpdateRequest req);
    void changePassword(UserPasswordRequest req);
    void delete(Long id);
}
