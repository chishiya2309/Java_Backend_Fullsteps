package vn.hunghaohan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.request.UserPasswordRequest;
import vn.hunghaohan.controller.request.UserUpdateRequest;
import vn.hunghaohan.controller.response.ApiResponse;
import vn.hunghaohan.controller.response.UserResponse;
import vn.hunghaohan.service.UserService;

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
    public ApiResponse getList(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String sort,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "20") int size) {
        log.info("Get user list");

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("user list")
                .data(userService.findAll(keyword, sort, page, size))
                .build();
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get user detail", description = "Lấy thông tin chi tiết của user theo id")
    @GetMapping("/{userId}")
    public ApiResponse getUserDetail(@PathVariable @Min(value = 1, message = "userId must be equals or greater than 1") Long userId) {
        log.info("Get user detail by ID: {}", userId);
        UserResponse data = userService.findById(userId);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("user detail")
                .data(data)
                .build();
    }

    @Operation(summary = "Create new user", description = "Tạo mới một user")
    @PostMapping("/create")
    public ApiResponse createUser(@RequestBody @Valid UserCreationRequest request) {
        log.info("Request to create user: {}", request);

        return ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("create user successfully")
                .data(userService.save(request))
                .build();
    }

    @GetMapping("/confirm-email")
    public void confirmEmail(@RequestParam String secretCode, HttpServletResponse response) {
        log.info("Confirming email with secret code: {}", secretCode);
        try {
            userService.confirmEmail(secretCode);
            response.sendRedirect("https://google.com");
        }catch(Exception e) {
            log.error("Email confirmation failed {}", e.getMessage());
            throw new IllegalArgumentException("Invalid or expired email confirmation code");
        }
    }

    @Operation(summary = "Update user", description = "Cập nhật thông tin của user")
    @PutMapping("/update")
    public ApiResponse updateUser(@RequestBody @Valid UserUpdateRequest request) {
        log.info("Request to update user: {}", request);

        userService.update(request);

        return ApiResponse.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message("update user successfully")
                .data("")
                .build();
    }

    @Operation(summary = "Change Password", description = "Thay đổi mật khẩu của user")
    @PatchMapping("/change-password")
    public ApiResponse changePassword(@RequestBody @Valid UserPasswordRequest request) {
        log.info("Request to change password: {}", request);

        userService.changePassword(request);

        return ApiResponse.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("change password successfully")
                .data("")
                .build();
    }

    @Operation(summary = "Delete user", description = "Xóa user theo id")
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse deleteUser(@PathVariable @Min(value = 1, message = "userId must be greater than 0") Long userId) {
        log.info("Deleting user: {}", userId);

        userService.delete(userId);

        return ApiResponse.builder()
                .status(HttpStatus.RESET_CONTENT.value())
                .message("delete user successfully")
                .data("")
                .build();
    }
}
