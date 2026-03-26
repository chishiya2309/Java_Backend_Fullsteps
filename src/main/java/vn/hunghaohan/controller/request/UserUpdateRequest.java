package vn.hunghaohan.controller.request;

import lombok.Getter;
import lombok.ToString;
import vn.hunghaohan.common.Gender;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@ToString
public class UserUpdateRequest implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
    private List<AddressRequest> addresses;
}
