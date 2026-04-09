package vn.hunghaohan.controller.response;

import lombok.*;
import vn.hunghaohan.common.Gender;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
}
