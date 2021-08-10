package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.PrivilegeDto;
import com.agatah.dogtalk.model.Privilege;

public class PrivilegeMapper {

    public static PrivilegeDto toPrivilegeDto(Privilege privilege){
        return new PrivilegeDto()
                .setPrivilegeId(privilege.getId())
                .setPrivilegeType(privilege.getPrivilegeType())
                .setBehavioristId(privilege.getBehaviorist().getId())
                .setSchoolName(privilege.getSchool().getName())
                .setSchoolId(privilege.getSchool().getId());
    }
}
