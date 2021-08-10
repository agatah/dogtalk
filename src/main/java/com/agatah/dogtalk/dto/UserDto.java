package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
public class UserDto {

    private Long id;

    @Email(message = "email should be valid", groups = UserRegister.class)
    @Size(min = 5, message = "email should be at least 5 characters long", groups = UserRegister.class)
    private String email;

    @Size(min = 7, message = "password should be at least 7 characters long", groups = {PasswordInfo.class, UserRegister.class})
    private String password;

    private String passwordRepeat;

    @Size(min = 2, message = "first name should be at least 2 characters long", groups = {UserInfo.class, UserRegister.class})
    private String firstName;

    @Size(min = 2, message = "last name should be at least 2 characters long", groups = {UserInfo.class, UserRegister.class})
    private String lastName;

    private Boolean passwordSame;

    private Long photoId;

    private String role;

    @AssertTrue(message = "repeated password should be the same", groups = {PasswordInfo.class, UserRegister.class})
    private boolean isPasswordSame(){
        passwordSame = password.equals(passwordRepeat);
        return passwordSame;
    }

    public interface UserInfo{}
    public interface PasswordInfo{}
    public interface UserRegister{}
}
