package vn.hunghaohan.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.hunghaohan.common.Gender;
import vn.hunghaohan.common.UserStatus;
import vn.hunghaohan.common.UserType;
import vn.hunghaohan.controller.request.AddressRequest;
import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.response.UserPageResponse;
import vn.hunghaohan.exception.ResourceNotFoundException;
import vn.hunghaohan.model.UserEntity;
import vn.hunghaohan.repository.AddressRepository;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.impl.UserServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    private @Mock UserRepository userRepository;
    private @Mock AddressRepository addressRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;


    private static UserEntity hungHaoHan;
    private static UserEntity jack97;

    @BeforeAll
    static void beforeAll() {
        hungHaoHan = new UserEntity();
        hungHaoHan.setId(1L);
        hungHaoHan.setFirstName("Hưng");
        hungHaoHan.setLastName("Hào Hán");
        hungHaoHan.setGender(Gender.MALE);
        hungHaoHan.setBirthDay(Date.valueOf(LocalDate.of(2005, 9, 23)));
        hungHaoHan.setUserName("hunghaohan");
        hungHaoHan.setPassword("12345678");
        hungHaoHan.setEmail("youngboycodon");
        hungHaoHan.setPhone("0329399399");
        hungHaoHan.setType(UserType.ADMIN);
        hungHaoHan.setStatus(UserStatus.ACTIVE);

        jack97 = new UserEntity();
        jack97.setId(2L);
        jack97.setFirstName("Jack");
        jack97.setLastName("97");
        jack97.setGender(Gender.MALE);
        jack97.setBirthDay(Date.valueOf(LocalDate.of(2005, 9, 23)));
        jack97.setUserName("j97so1");
        jack97.setPassword("12345678");
        jack97.setEmail("hathaynhungbocon");
        jack97.setPhone("0123456789");
        jack97.setType(UserType.USER);
        jack97.setStatus(UserStatus.ACTIVE);

    }

    @BeforeEach
    void setUp() {
        // Khởi tạo bước triển khai la user Service
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetListUsers_Success() {
        // Giả lập phương thức
        Page<UserEntity> userPage = new PageImpl<>(Arrays.asList(hungHaoHan, jack97));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Gọi phương thức cần test
        UserPageResponse result = userService.findAll(null, null, 0, 20);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testSearchUser_Success() {
        // Giả lập phương thức
        Page<UserEntity> userPage = new PageImpl<>(Arrays.asList(hungHaoHan, jack97));
        when(userRepository.searchByKeyword(any(), any(Pageable.class))).thenReturn(userPage);

        // Gọi phương thức cần test
        UserPageResponse result = userService.findAll("Hưng", null, 0, 20);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetListUsers_Empty() {
        // Giả lập phương thức
        Page<UserEntity> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Gọi phương thức cần test
        UserPageResponse result = userService.findAll(null, null, 0, 20);
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(hungHaoHan));

        var result = userService.findById(1L);
        assertNotNull(result);
        assertEquals("Hưng", result.getFirstName());
    }

    @Test
    void testGetUserById_Failure() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.findById(999L));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void findByUsername() {
    }

    @Test
    void testSaveUser_Success() {
        // Giả lập phương thức
        when(userRepository.save(any(UserEntity.class))).thenReturn(hungHaoHan);

        // Tạo request
        UserCreationRequest request = new UserCreationRequest();
        request.setFirstName("Hưng");
        request.setLastName("Lê");
        request.setGender(Gender.MALE);
        request.setBirthDay(Date.valueOf(LocalDate.of(2005, 9, 23)));
        request.setEmail("lequanghung.work@gmail.com");
        request.setPhone("0347983243");
        request.setUsername("hunghaohan");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("411");
        addressRequest.setFloor("4");
        addressRequest.setBuilding("Bekind");
        addressRequest.setStreetNumber("17");
        addressRequest.setStreet("Đường số 7");
        addressRequest.setCity("Hồ Chí Minh");
        addressRequest.setCountry("Việt Nam");
        addressRequest.setAddressType(1);
        request.setAddresses(List.of(addressRequest));

        Long userId = userService.save(request);
        assertEquals(1L, userId);

    }

    @Test
    void confirmEmail() {
    }

    @Test
    void update() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void delete() {
    }
}