package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.BehavioristShortProfileDto;
import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.Privilege;

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
                .setSchools(behaviorist.getPrivileges()
                        .stream()
                        .map(Privilege::getSchool)
                        .map(SchoolMapper::toSchoolShortDto)
                        .collect(Collectors.toList()))
                .setUserId(behaviorist.getUser().getId())
                .setFirstName(behaviorist.getUser().getFirstName())
                .setLastName(behaviorist.getUser().getLastName())
                .setPrivileges(behaviorist.getPrivileges()
                        .stream()
                        .map(PrivilegeMapper::toPrivilegeDto)
                        .collect(Collectors.toList()));

        if(behaviorist.getUser().getPetOwnerProfile() != null){
            profileDto.setOwnerId(behaviorist.getUser().getPetOwnerProfile().getId());
        }
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
