package com.agatah.dogtalk.dto;

import com.agatah.dogtalk.model.enums.PrivilegeType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SchoolWithPrivilegesDto {

    private List<PrivilegeType> privileges = new ArrayList<>();
    private String schoolName;
    private Long schoolId;
}
