package vn.hunghaohan.service;

import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.request.UserPasswordRequest;
import vn.hunghaohan.controller.request.UserUpdateRequest;
import vn.hunghaohan.controller.response.UserPageResponse;
import vn.hunghaohan.controller.response.UserResponse;

import java.util.List;

public interface UserService {

    UserPageResponse findAll(String keyword, String sort, int page, int size);

    UserResponse findById(Long id);

    UserResponse findByUsername(String username);

    Long save(UserCreationRequest req);

    void update(UserUpdateRequest req);

    void changePassword(UserPasswordRequest req);

    void delete(Long id);
}
