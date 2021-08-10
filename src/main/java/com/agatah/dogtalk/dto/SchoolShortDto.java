package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SchoolShortDto {

    private Long id;
    private String name;
    private List<LocationDto> locations;

}
