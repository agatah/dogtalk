package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SchoolFullDto {

    private Long id;
    private String name;
    private List<BehavioristShortProfileDto> behaviorists;
    private List<ServiceDto> services;
    private List<ContactDto> contacts;
    private List<LocationDto> locations;
    private Long bannerId;

}
