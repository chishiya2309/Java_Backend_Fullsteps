package vn.hunghaohan.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hunghaohan.common.Gender;
import vn.hunghaohan.common.UserType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserCreationRequest implements Serializable {
    @NotBlank(message = "firstName must be not blank")
    private String firstName;

    @NotBlank(message = "lastName must be not blank")
    private String lastName;
    private Date birthDay;
    private Gender gender;
    private String phone;
    @Email(message = "Email invalid")
    private String email;
    private String username;
    private UserType type;
    private String password;
    private List<AddressRequest> addresses; //home, office
}
