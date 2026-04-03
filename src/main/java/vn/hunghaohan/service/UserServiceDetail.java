package vn.hunghaohan.service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.hunghaohan.repository.UserRepository;

@Service
public record UserServiceDetail(UserRepository userRepository) {

    public UserDetailsService userServiceDetail() {
        return userRepository::findByUserName;
    }
}
