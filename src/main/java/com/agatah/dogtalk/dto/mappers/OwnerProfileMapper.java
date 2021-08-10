package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.OwnerProfileDto;
import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.model.PetOwnerProfile;

import java.util.Comparator;
import java.util.stream.Collectors;

public class OwnerProfileMapper {

    public static OwnerProfileDto toOwnerProfileDto(PetOwnerProfile petOwnerProfile){
        return new OwnerProfileDto()
                .setOwnerId(petOwnerProfile.getId())
                .setUserId(petOwnerProfile.getUser().getId())
                .setPets(petOwnerProfile.getPets()
                        .stream()
                        .map(PetProfileMapper::toPetProfileDto)
                        .sorted(Comparator.comparing(PetProfileDto::getPetId))
                        .collect(Collectors.toList()));
    }
}
