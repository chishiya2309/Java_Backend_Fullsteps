package vn.hunghaohan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.request.UserPasswordRequest;
import vn.hunghaohan.controller.request.UserUpdateRequest;
import vn.hunghaohan.controller.response.UserPageResponse;
import vn.hunghaohan.controller.response.UserResponse;
import vn.hunghaohan.service.UserService;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
@RequiredArgsConstructor
@Slf4j(topic = "USER-CONTROLLER")
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user list", description = "Lấy danh sách user với các tham số tùy chọn: keyword, sort, page, size")
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        log.info("Get user list");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user list");
        result.put("data", userService.findAll(keyword, sort, page, size));

        return result;
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get user detail", description = "Lấy thông tin chi tiết của user theo id")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable @Min(value = 1, message = "userId must be equals or greater than 1") Long userId) {
        log.info("Get user detail by ID: {}", userId);
        UserResponse data = userService.findById(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user detail");
        result.put("data", data);

        return result;
    }

    @Operation(summary = "Create new user", description = "Tạo mới một user")
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Request to create user: {}", request);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "create user successfully");
        result.put("data", userService.save(request));

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) throws IOException {
        log.info("Confirming email with secret code: {}", secretCode);
        try {
            userService.confirmEmail(secretCode);
            response.sendRedirect("https://google.com");
        }catch(Exception e) {
            log.error("Email confirmation failed {}", e.getMessage());
            throw new RuntimeException("Invalid or expired email confirmation code");
        }
    }

    @Operation(summary = "Update user", description = "Cập nhật thông tin của user")
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody @Valid UserUpdateRequest request) {
        log.info("Request to update user: {}", request);

        userService.update(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.ACCEPTED.value());
        result.put("message", "update user successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Change Password", description = "Thay đổi mật khẩu của user")
    @PatchMapping("/change-password")
    public Map<String, Object> changePassword(@RequestBody @Valid UserPasswordRequest request) {
        log.info("Request to change password: {}", request);

        userService.changePassword(request);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.NO_CONTENT.value());
        result.put("message", "change password successfully");
        result.put("data", "");

        return result;
    }

    @Operation(summary = "Delete user", description = "Xóa user theo id")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Map<String, Object> deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") Long userId) {
        log.info("Deleting user: {}", userId);

        userService.delete(userId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.RESET_CONTENT.value());
        result.put("message", "delete user successfully");
        result.put("data", "");

        return result;
    }
}
