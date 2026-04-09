package vn.hunghaohan.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vn.hunghaohan.common.Gender;
import vn.hunghaohan.controller.response.UserPageResponse;
import vn.hunghaohan.controller.response.UserResponse;
import vn.hunghaohan.service.JwtService;
import vn.hunghaohan.service.UserService;
import vn.hunghaohan.service.UserServiceDetail;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static vn.hunghaohan.common.Gender.MALE;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserServiceDetail userServiceDetail;

    @MockitoBean
    private JwtService jwtService;

    private static UserResponse hungHaoHan;
    private static UserResponse johnDoe;

    @BeforeAll
    static void setUp() {
        // Chuẩn bị dữ liệu
        hungHaoHan = new UserResponse();
        hungHaoHan.setId(1L);
        hungHaoHan.setFirstName("Hưng");
        hungHaoHan.setLastName("Lê");
        hungHaoHan.setGender(MALE);
        hungHaoHan.setBirthDay(Date.valueOf(LocalDate.of(2005, 9, 23)));
        hungHaoHan.setEmail("lequanghung.work@gmail.com");
        hungHaoHan.setPhone("0347983243");
        hungHaoHan.setUsername("Youngboycodon");

        johnDoe = new UserResponse();
        johnDoe.setId(2L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setGender(Gender.MALE);
        johnDoe.setBirthDay(Date.valueOf(LocalDate.of(1990, 1, 1)));
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPhone("0123456789");
        johnDoe.setUsername("johndoe");
    }

    @Test
    @WithMockUser(authorities = {"ADMIN", "MANAGER"})
    void testGetUser() throws Exception {
        List<UserResponse> userResponses = List.of(hungHaoHan, johnDoe);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setTotalPages(1);
        userPageResponse.setTotalElements(2);
        userPageResponse.setUsers(userResponses);

        when(userService.findAll(null, null, 0, 20)).thenReturn(userPageResponse);

        mockMvc.perform(get("/user/list").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("user list"))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }
}
