package com.agatah.dogtalk.dto;

import com.agatah.dogtalk.model.enums.PrivilegeType;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class PrivilegeDto {

    private Long privilegeId;
    private Long behavioristId;
    private PrivilegeType privilegeType;
    private String schoolName;
    private Long schoolId;
}
