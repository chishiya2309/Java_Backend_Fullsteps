package vn.hunghaohan.controller.request.user;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String gender;
    private String phone;
    private String email;
    private String username;
}
