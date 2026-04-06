package vn.hunghaohan.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import vn.hunghaohan.common.Gender;
import vn.hunghaohan.common.UserStatus;
import vn.hunghaohan.common.UserType;
import vn.hunghaohan.model.UserEntity;
import vn.hunghaohan.repository.AddressRepository;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

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
        hungHaoHan.setLastName("Hào Hân");
        hungHaoHan.setGender(Gender.MALE);
        hungHaoHan.setBirthDay(new java.util.Date(2005, 9, 23));
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
        jack97.setBirthDay(new java.util.Date(2005, 9, 23));
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
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void save() {
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