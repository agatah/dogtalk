package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.dto.mappers.PetProfileMapper;
import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.Pet;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.repository.OwnerProfileRepository;
import com.agatah.dogtalk.repository.PetProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

    private PetProfileRepository petProfileRepository;
    private OwnerProfileRepository ownerProfileRepository;
    private PhotoService photoService;

    public PetService(PetProfileRepository petProfileRepository, OwnerProfileRepository ownerProfileRepository,
                      PhotoService photoService){
        this.petProfileRepository = petProfileRepository;
        this.ownerProfileRepository = ownerProfileRepository;
        this.photoService = photoService;
    }

    public PetProfileDto createPetProfile(Long ownerId){
        Optional<PetOwnerProfile> ownerProfileOpt = ownerProfileRepository.findById(ownerId);
        if(ownerProfileOpt.isPresent()){
            Pet pet = new Pet().setOwner(ownerProfileOpt.get());
            return PetProfileMapper.toPetProfileDto(petProfileRepository.save(pet));
        } else {
            return null;
        }
    }

    public void deletePetProfile(Long petId){
        petProfileRepository.deleteById(petId);
    }

    public PetProfileDto updatePetProfile(PetProfileDto petProfileDto){
        Optional<Pet> petProfileOpt = petProfileRepository.findById(petProfileDto.getPetId());
        if(petProfileOpt.isPresent()){
            Pet dbPet = petProfileOpt.get();
            dbPet.setName(petProfileDto.getName());
            return PetProfileMapper.toPetProfileDto(petProfileRepository.save(dbPet));
        } else {
            return null;
        }
    }

    public PetProfileDto addPhoto(Long petId, Photo photo){
        Optional<Pet> petOpt = petProfileRepository.findById(petId);
        if(petOpt.isPresent()){
            PetProfileMapper.toPetProfileDto(petProfileRepository.save(petOpt.get().addPhoto(photoService.uploadPhoto(photo))));
        }
        return null;
    }
}
