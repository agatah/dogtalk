package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SchoolForm {

    @NotNull(message = "school's name must not be null")
    @Size(min = 5, message = "school name must be at least 5 characters long")
    private String formSchoolName;

    @Valid
    @NotEmpty(message = "you must enter at least one city")
    private List<LocationDto> locations = new ArrayList<>();

}
