package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.OwnerProfileDto;
import com.agatah.dogtalk.dto.mappers.OwnerProfileMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.OwnerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerProfileRepository ownerProfileRepository;

    @Autowired
    public OwnerService(OwnerProfileRepository ownerProfileRepository){
        this.ownerProfileRepository = ownerProfileRepository;
    }

    public OwnerProfileDto getOwnerById(Long id){
        Optional<PetOwnerProfile> ownerProfileOpt = ownerProfileRepository.findById(id);
        return ownerProfileOpt
                .map(OwnerProfileMapper::toOwnerProfileDto)
                .orElseThrow(() -> new EntityNotFoundException(PetOwnerProfile.class, id));
    }

    public OwnerProfileDto createOwnerProfile(User user) {
        PetOwnerProfile petOwnerProfile = new PetOwnerProfile()
                .setUser(user);
        return OwnerProfileMapper.toOwnerProfileDto(ownerProfileRepository.save(petOwnerProfile));
    }

    public void deleteById(Long userId){
        if(ownerProfileRepository.existsById(userId)) {
            ownerProfileRepository.deleteById(userId);
        }
    }

}
