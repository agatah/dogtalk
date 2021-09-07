package com.agatah.dogtalk.dto;

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
    private List<SchoolWithPrivilegesDto> schoolWithPrivilegesList;
    private String firstName;
    private String lastName;
    private Long photoId;

    public String getFullName(){
        return firstName + " " + lastName;
    }

}
