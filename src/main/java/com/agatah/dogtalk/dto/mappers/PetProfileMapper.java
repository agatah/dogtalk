package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.model.Pet;
import com.agatah.dogtalk.model.Photo;

import java.util.stream.Collectors;

public class PetProfileMapper {

    public static PetProfileDto toPetProfileDto(Pet pet){
        return new PetProfileDto()
                .setPetId(pet.getId())
                .setOwnerId(pet.getOwner().getId())
                .setName(pet.getName())
                .setPhotoIds(pet.getPhotos()
                        .stream()
                        .map(Photo::getId)
                        .collect(Collectors.toList()));
    }
}
