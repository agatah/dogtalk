package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class SchoolFullDto {

    private Long id;
    private String name;
    private List<BehavioristShortProfileDto> behaviorists;
    private List<BehavioristShortProfileDto> behavioristsRequested;
    private List<ServiceDto> services;
    private List<ContactDto> contacts;
    private List<LocationDto> locations;
    private Long bannerId;

    public List<Long> getBehavioristIds(){
        return behaviorists.stream()
                .map(BehavioristShortProfileDto::getId)
                .collect(Collectors.toList());
    }

    public List<Long> getBehavioristRequestedIds(){
        return behavioristsRequested.stream()
                .map(BehavioristShortProfileDto::getId)
                .collect(Collectors.toList());
    }

}
