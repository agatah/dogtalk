package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.SchoolFullDto;
import com.agatah.dogtalk.dto.SchoolShortDto;
import com.agatah.dogtalk.model.BehavioristPrivilegesInSchool;
import com.agatah.dogtalk.model.School;

import java.util.stream.Collectors;

public class SchoolMapper {

    public static SchoolFullDto toSchoolFullDto(School school){
        SchoolFullDto schoolFullDto = new SchoolFullDto()
                .setId(school.getId())
                .setName(school.getName())
                .setBehaviorists(school.getBehavioristPrivilegesInSchools()
                        .stream()
                        .filter(s -> !s.hasPrivilegeJoinRequest())
                        .map(BehavioristPrivilegesInSchool::getBehaviorist)
                        .map(BehavioristProfileMapper::toBehavioristShortProfileDto)
                        .collect(Collectors.toList()))
                .setBehavioristsRequested(school.getBehavioristPrivilegesInSchools()
                        .stream()
                        .filter(BehavioristPrivilegesInSchool::hasPrivilegeJoinRequest)
                        .map(BehavioristPrivilegesInSchool::getBehaviorist)
                        .map(BehavioristProfileMapper::toBehavioristShortProfileDto)
                        .collect(Collectors.toList()))
                .setServices(school.getServiceOffers()
                        .stream()
                        .map(ServiceMapper::toServiceDto)
                        .collect(Collectors.toList()))
                .setLocations(school.getLocations()
                        .stream()
                        .map(LocationMapper::toLocationDto)
                        .collect(Collectors.toList()))
                .setContacts(school.getContacts()
                        .stream()
                        .map(ContactMapper::toContactDto)
                        .collect(Collectors.toList()));

        if(school.getBanner() != null){
            schoolFullDto.setBannerId(school.getBanner().getId());
        }

        return schoolFullDto;
    }

    public static SchoolShortDto toSchoolShortDto(School school){
        return new SchoolShortDto()
                .setId(school.getId())
                .setName(school.getName())
                .setLocations(school.getLocations()
                    .stream()
                    .map(LocationMapper::toLocationDto)
                    .collect(Collectors.toList()));
    }
}
