package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.model.Pet;
import com.agatah.dogtalk.model.Photo;

import java.util.stream.Collectors;

public class PetProfileMapper {

    public static PetProfileDto toPetProfileDto(Pet pet){
        PetProfileDto petProfileDto = new PetProfileDto()
                .setPetId(pet.getId())
                .setOwnerId(pet.getOwner().getId())
                .setPetName(pet.getName())
                .setAge(pet.getAge())
                .setBreed(pet.getBreed());
        if(pet.getPhoto() != null){
            petProfileDto.setPhotoId(pet.getPhoto().getId());
        }
        return petProfileDto;
    }
}
