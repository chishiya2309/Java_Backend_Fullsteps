package vn.hunghaohan.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {
    private String password;
    private String platform; //web, mobile, miniApp
    private String deviceToken;
    private String versionApp;
    private String username;
}
