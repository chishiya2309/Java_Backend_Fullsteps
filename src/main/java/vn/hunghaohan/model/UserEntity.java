package vn.hunghaohan.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.hunghaohan.common.Gender;
import vn.hunghaohan.common.UserStatus;
import vn.hunghaohan.common.UserType;

import java.io.Serializable;
import java.util.*;
import java.util.Locale;

@Getter
@Setter

@Entity
@Table(name = "tbl_user")
public class UserEntity extends AbstractEntity<Long> implements UserDetails, Serializable  {

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private Date birthDay;

    @Column(name = "username", unique = true, nullable = false, length = 255)
    private String userName;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "secret_code", length = 255)
    private String secretCode;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", length = 255)
    private UserType type;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", length = 255)
    private UserStatus status;

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<UserHasRole> roles = new HashSet<>();

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        //1. Get Role
        List<Role> roleList = roles.stream().map(UserHasRole::getRoleId).toList();

        // 2. Normalize role names to avoid case/whitespace mismatches in @PreAuthorize checks
        List<String> roleNames = roleList.stream()
                .map(Role::getName)
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(roleName -> roleName.toUpperCase(Locale.ROOT))
                .toList();

        //3. Add role name to authority
        return roleNames.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public @NonNull String getUsername() {
        return userName;
    }

    @Override
    public @NonNull String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE.equals(status);
    }
}
