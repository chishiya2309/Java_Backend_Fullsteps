package vn.hunghaohan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hunghaohan.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
