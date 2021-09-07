package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.BehavioristShortProfileDto;
import com.agatah.dogtalk.model.BehavioristProfile;

import java.util.stream.Collectors;

public class BehavioristProfileMapper {

    public static BehavioristFullProfileDto toBehavioristFullProfileDto(BehavioristProfile behaviorist){
        BehavioristFullProfileDto profileDto = new BehavioristFullProfileDto()
                .setId(behaviorist.getId())
                .setContacts(behaviorist.getContactList()
                        .stream()
                        .map(ContactMapper::toContactDto)
                        .collect(Collectors.toList()))
                .setAbout(behaviorist.getAbout())
                .setQualifications(behaviorist.getQualifications())
                .setFirstName(behaviorist.getUser().getFirstName())
                .setLastName(behaviorist.getUser().getLastName())
                .setSchoolWithPrivilegesList(behaviorist.getBehavioristPrivilegesInSchools()
                        .stream()
                        .filter(s -> !s.hasPrivilegeJoinRequest())
                        .map(PrivilegeMapper::toSchoolWithPrivilegesDto)
                        .collect(Collectors.toList()));


        if(behaviorist.getUser().getPhoto() != null){
            profileDto.setPhotoId(behaviorist.getUser().getPhoto().getId());
        }

        return profileDto;
    }

    public static BehavioristShortProfileDto toBehavioristShortProfileDto(BehavioristProfile behaviorist){
        BehavioristShortProfileDto profileDto = new BehavioristShortProfileDto()
                .setId(behaviorist.getId())
                .setAbout(behaviorist.getAbout())
                .setFirstName(behaviorist.getUser().getFirstName())
                .setLastName(behaviorist.getUser().getLastName());

        if(behaviorist.getUser().getPhoto() != null){
            profileDto.setPhotoId(behaviorist.getUser().getPhoto().getId());
        }

        return profileDto;
    }
}
