package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.SchoolWithPrivilegesDto;
import com.agatah.dogtalk.model.BehavioristPrivilegesInSchool;
import com.agatah.dogtalk.model.Privilege;

import java.util.stream.Collectors;

public class PrivilegeMapper {

    public static SchoolWithPrivilegesDto toSchoolWithPrivilegesDto(BehavioristPrivilegesInSchool behavioristPrivilegesInSchool){
        return new SchoolWithPrivilegesDto()
                .setPrivileges(behavioristPrivilegesInSchool.getPrivileges()
                        .stream()
                        .map(Privilege::getPrivilegeType)
                        .collect(Collectors.toList()))
                .setSchoolName(behavioristPrivilegesInSchool.getSchool().getName())
                .setSchoolId(behavioristPrivilegesInSchool.getSchool().getId());
    }
}
