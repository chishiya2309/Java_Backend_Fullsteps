package vn.hunghaohan.controller.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String gender;
    private String phone;
    private String email;
    private String username;
}
