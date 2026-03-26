package vn.hunghaohan.controller.request;

import lombok.Getter;
import vn.hunghaohan.common.Gender;
import vn.hunghaohan.common.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private Date birthDay;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
    private UserType type;
    private String password;
    private List<AddressRequest> addresses; //home, office
}
