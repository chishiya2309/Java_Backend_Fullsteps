package vn.hunghaohan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hunghaohan.model.AddressEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
