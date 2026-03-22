package vn.hunghaohan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.response.UserResponse;

import java.util.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User Controller")
public class UserController {

    @Operation(summary = "Test API", description = "Mô tả chi tiết")
    @GetMapping("/list")
    public Map<String, Object> getList(@RequestParam(required = false) String keyword,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(1L);
        userResponse1.setFirstName("Hưng");
        userResponse1.setLastName("Lê");
        userResponse1.setBirthDay(new Date(2005, 9, 23));
        userResponse1.setGender("Male");
        userResponse1.setPhone("0347983243");
        userResponse1.setEmail("lequanghung.work@gmail.com");
        userResponse1.setUsername("youngboycodon");

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2L);
        userResponse2.setFirstName("Hưng");
        userResponse2.setLastName("Hảo");
        userResponse2.setBirthDay(new Date(2005, 9, 23));
        userResponse2.setGender("Male");
        userResponse2.setPhone("0347983113");
        userResponse2.setEmail("lequanghungtest.work@gmail.com");
        userResponse2.setUsername("youngboysitinh");
        List<UserResponse> userList = List.of(userResponse1, userResponse2);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user list");
        result.put("data", userList);

        return result;
    }

    @Operation(summary = "Get user detail", description = "Lấy thông tin chi tiết của user theo id")
    @GetMapping("/{userId}")
    public Map<String, Object> getUserDetail(@PathVariable Long userId) {
        UserResponse userDetail = new UserResponse();
        userDetail.setUsername("youngboycodon");
        userDetail.setLastName("Lê");
        userDetail.setId(userId);
        userDetail.setGender("MALE");
        userDetail.setPhone("0347983243");
        userDetail.setEmail("lequanghung.work@gmail.com");
        userDetail.setBirthDay(new Date(2005, 9, 23));
        userDetail.setFirstName("Hưng");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.OK.value());
        result.put("message", "user detail");
        result.put("data", userDetail);

        return result;
    }

    @Operation(summary = "Create new user", description = "Tạo mới một user")
    @PostMapping("/create")
    public Map<String, Object> createUser(UserCreationRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", HttpStatus.CREATED.value());
        result.put("message", "create user successfully");
        result.put("data", 3);

        return result;
    }
}
