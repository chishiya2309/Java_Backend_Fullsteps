package vn.hunghaohan.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hunghaohan.common.UserStatus;
import vn.hunghaohan.common.UserType;
import vn.hunghaohan.controller.request.UserCreationRequest;
import vn.hunghaohan.controller.request.UserPasswordRequest;
import vn.hunghaohan.controller.request.UserUpdateRequest;
import vn.hunghaohan.controller.response.UserResponse;
import vn.hunghaohan.model.AddressEntity;
import vn.hunghaohan.model.UserEntity;
import vn.hunghaohan.repository.AddressRepository;
import vn.hunghaohan.repository.UserRepository;
import vn.hunghaohan.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

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
    @Transactional(rollbackFor = Exception.class)
    public Long save(UserCreationRequest req) {
        log.info("Saving user: {}", req);
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
