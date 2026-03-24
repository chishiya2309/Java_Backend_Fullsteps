package vn.hunghaohan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "first_name", length = 255)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate birthDay;

    @Column(name = "user_name", unique = true, nullable = false, length = 255)
    private String userName;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "type", length = 255)
    private UserType type;

    @Column(name = "status", length = 255)
    private UserStatus status;

    @Column(name = "created_at", length = 255)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", length = 255)
    private Instant updatedAt;
}
