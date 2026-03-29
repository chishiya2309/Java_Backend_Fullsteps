package vn.hunghaohan.service.impl;

import ch.qos.logback.core.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.hunghaohan.common.UserStatus;
import vn.hunghaohan.common.UserType;
import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.request.UserPasswordRequest;
import vn.hunghaohan.controller.request.UserUpdateRequest;
import vn.hunghaohan.controller.response.UserPageResponse;
import vn.hunghaohan.controller.response.UserResponse;
import vn.hunghaohan.exception.InvalidDataException;
import vn.hunghaohan.exception.ResourceNotFoundException;
import vn.hunghaohan.model.AddressEntity;
import vn.hunghaohan.model.UserEntity;
import vn.hunghaohan.repository.AddressRepository;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {
        log.info("Finding all users with keyword: {}, sort: {}, pageNo: {}, pageSize: {}", keyword, sort, page, size);

        // Sorting
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if(StringUtils.hasLength(sort)) {
            // Gọi search method
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)"); // tên cột:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    order = new Sort.Order(Sort.Direction.ASC, columnName);
                }else {
                    order = new Sort.Order(Sort.Direction.DESC, columnName);
                }
            }
        }

        // Xử lý trường hợp FE muốn bắt đầu với page = 1
        int pageNo = 0;
        if (page > 0) {
            pageNo = page - 1;
        }

        // Paging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));

        Page<UserEntity> entityPage;

        if(StringUtils.hasLength(keyword)) {
            // Gọi search method
            entityPage = userRepository.searchByKeyword("%" + keyword.toLowerCase() + "%", pageable);
        }else {
            entityPage = userRepository.findAll(pageable);
        }

        return getUserPageResponse(size, entityPage, pageNo);
    }

    @Override
    public UserResponse findById(Long id) {
        log.info("Finding user with id: {}", id);
        UserEntity userEntity = getUserEntity(id);
        return UserResponse.builder()
                .id(id)
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .gender(String.valueOf(userEntity.getGender()))
                .birthDay(userEntity.getBirthDay())
                .username(userEntity.getUserName())
                .email(userEntity.getEmail())
                .phone(userEntity.getPhone())
                .build();
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserCreationRequest req) {
        log.info("Saving user: {}", req);

        UserEntity userByEmail = userRepository.findByEmail(req.getEmail());
        if (userByEmail != null) {
            throw new InvalidDataException("Email already exists");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setGender(req.getGender());
        user.setBirthDay(req.getBirthDay());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setUserName(req.getUsername());
        user.setPassword(req.getPassword());
        user.setType(req.getType());
        user.setStatus(UserStatus.NONE);
        userRepository.save(user);
        log.info("Saved user: {}", user);
;
        if(user.getId() != null) {
            log.info("user id: {}", user.getId());
            List<AddressEntity> addreses = new ArrayList<>();
            req.getAddresses().forEach(address -> {
                AddressEntity addressEntity = new AddressEntity();
                addressEntity.setApartmentNumber(address.getApartmentNumber());
                addressEntity.setFloor(address.getFloor());
                addressEntity.setBuilding(address.getBuilding());
                addressEntity.setStreetNumber(address.getStreetNumber());
                addressEntity.setStreet(address.getStreet());
                addressEntity.setCity(address.getCity());
                addressEntity.setCountry(address.getCountry());
                addressEntity.setAddressType(address.getAddressType());
                addressEntity.setUserId(user.getId());
                addreses.add(addressEntity);
            });
            addressRepository.saveAll(addreses);
            log.info("Saved addresses: {}", addreses);
        }

        return 1L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest req) {
        log.info("Updating user: {}", req);

        // Get user by id
        UserEntity user = getUserEntity(req.getId());

        // set data
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setGender(req.getGender());
        user.setBirthDay(req.getBirthDay());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setUserName(req.getUsername());

        userRepository.save(user);
        log.info("Updated user: {}", user);

        // save address

        List<AddressEntity> addresses = new ArrayList<>();

        req.getAddresses().forEach(address -> {
            AddressEntity addressEntity = addressRepository.findByUserIdAndAddressType(req.getId(), address.getAddressType());
            if (addressEntity == null) {
                addressEntity = new AddressEntity();
            }

            addressEntity.setApartmentNumber(address.getApartmentNumber());
            addressEntity.setFloor(address.getFloor());
            addressEntity.setBuilding(address.getBuilding());
            addressEntity.setStreetNumber(address.getStreetNumber());
            addressEntity.setStreet(address.getStreet());
            addressEntity.setCity(address.getCity());
            addressEntity.setCountry(address.getCountry());
            addressEntity.setAddressType(address.getAddressType());
            addressEntity.setUserId(user.getId());

            addresses.add(addressEntity);
        });

        // Save addresses
        addressRepository.saveAll(addresses);
        log.info("Updated addresses: {}", addresses);
    }

    @Override
    public void changePassword(UserPasswordRequest req) {
        log.info("Changing password for user: {}", req);

        // Get user by id
        UserEntity user = getUserEntity(req.getId());

        if (req.getPassword().equals(req.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        userRepository.save(user);
        log.info("Changed password for user: {}", user);
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with id: {}", id);

        // Get user by id
        UserEntity user = getUserEntity(id);
        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
        log.info("Deleted soft user: {}", user);
    }

    /**
     * Get user by id
     * @param id
     * @return
     */
    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Convert  UserEntities to UserResponse
     * @param size
     * @param userPage
     * @param pageNo
     * @return
     */
    private static @NonNull UserPageResponse getUserPageResponse(int size, Page<UserEntity> userPage, int pageNo) {
        List<UserResponse> userList = userPage.stream().map(entity -> UserResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .gender(String.valueOf(entity.getGender()))
                .birthDay(entity.getBirthDay())
                .username(entity.getUserName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build()
        ).toList();

        UserPageResponse response = new UserPageResponse();
        response.setPageNumber(pageNo);
        response.setPageSize(size);
        response.setTotalPages(userPage.getTotalPages());
        response.setUsers(userList);
        return response;
    }
}
