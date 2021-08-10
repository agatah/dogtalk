package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.model.User;

public class UserMapper {

    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setId(user.getId());

        if(user.getPhoto() != null){
            userDto.setPhotoId(user.getPhoto().getId());
        }
        return userDto;
    }

    public static UserDetailsDto toUserDetailsDto(User user){
        return new UserDetailsDto()
                .setUserId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPassword(user.getPassword())
                .setRoles(user.getRoles());
    }
}
