package vn.hunghaohan.controller.request.user;

import lombok.Getter;
import vn.hunghaohan.controller.request.AddressRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
public class UserCreationRequest implements Serializable {
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String gender;
    private String phone;
    private String email;
    private String username;
    private List<AddressRequest> addresses; //home, office
}
