package vn.hunghaohan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hunghaohan.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(value = "select u from UserEntity u where u.status='ACTIVE' AND " +
            "lower(u.firstName) like :keyword " +
            "or lower(u.lastName) like :keyword " +
            "or u.phone like :keyword " +
            "or lower(u.email) like :keyword " +
            "or u.userName like :keyword")
    Page<UserEntity> searchByKeyword(String keyword, Pageable pageable);

    UserEntity findByEmail(String email);

    UserEntity findBySecretCode(String secretCode);

    UserEntity findByUserName(String userName);
}
