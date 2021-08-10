package com.agatah.dogtalk.dto;

import com.agatah.dogtalk.model.Contact;
import com.agatah.dogtalk.model.School;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class BehavioristFullProfileDto {

    private Long id;
    private List<ContactDto> contacts;
    private String about;
    private String qualifications;
    private List<SchoolShortDto> schools;
    private List<PrivilegeDto> privileges;
    private Long userId;
    private String firstName;
    private String lastName;
    private Long photoId;
    private Long ownerId;

    public String getFullName(){
        return firstName + " " + lastName;
    }

}
