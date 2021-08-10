package com.agatah.dogtalk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SchoolForm {

    @NotNull
    @Size(min = 5, message = "school name must be at least 5 characters long")
    private String formSchoolName;
}
