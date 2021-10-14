package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.dto.mappers.PetProfileMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.Pet;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.repository.OwnerProfileRepository;
import com.agatah.dogtalk.repository.PetProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

    private final PetProfileRepository petProfileRepository;
    private final OwnerProfileRepository ownerProfileRepository;
    private final PhotoService photoService;

    public PetService(PetProfileRepository petProfileRepository, OwnerProfileRepository ownerProfileRepository,
                      PhotoService photoService){
        this.petProfileRepository = petProfileRepository;
        this.ownerProfileRepository = ownerProfileRepository;
        this.photoService = photoService;
    }

    public PetProfileDto createPetProfile(Long ownerId, PetProfileDto petProfile){
        Optional<PetOwnerProfile> ownerProfileOpt = ownerProfileRepository.findById(ownerId);
        if(ownerProfileOpt.isPresent()){
            Pet pet = new Pet()
                    .setOwner(ownerProfileOpt.get())
                    .setName(petProfile.getPetName())
                    .setAge(petProfile.getAge())
                    .setBreed(petProfile.getBreed());
            return PetProfileMapper.toPetProfileDto(petProfileRepository.save(pet));
        } else {
            throw new EntityNotFoundException(PetOwnerProfile.class, ownerId);
        }
    }

    public void deletePetProfile(Long petId){
        if(petProfileRepository.existsById(petId)){
            petProfileRepository.deleteById(petId);
        }
    }

    public PetProfileDto updatePetProfile(PetProfileDto petProfileDto){
        Optional<Pet> petProfileOpt = petProfileRepository.findById(petProfileDto.getPetId());
        if(petProfileOpt.isPresent()){
            Pet dbPet = petProfileOpt.get();
            dbPet.setName(petProfileDto.getPetName())
                    .setAge(petProfileDto.getAge())
                    .setBreed(petProfileDto.getBreed());
            return PetProfileMapper.toPetProfileDto(petProfileRepository.save(dbPet));
        } else {
            throw new EntityNotFoundException(Pet.class, petProfileDto.getPetId());
        }
    }

    public PetProfileDto addPhoto(Long petId, Photo photo){
        Optional<Pet> petOpt = petProfileRepository.findById(petId);
        return petOpt
                .map(pet -> pet.setPhoto(photoService.uploadPhoto(photo)))
                .map(petProfileRepository::save)
                .map(PetProfileMapper::toPetProfileDto)
                .orElseThrow(() -> new EntityNotFoundException(Pet.class, petId));

    }
}
