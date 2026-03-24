package vn.hunghaohan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hunghaohan.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
