package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.OwnerProfileDto;
import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.dto.mappers.OwnerProfileMapper;
import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.OwnerProfileRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerService {

    private final OwnerProfileRepository ownerProfileRepository;
    private final UserRepository userRepository;

    public OwnerService(OwnerProfileRepository ownerProfileRepository, UserRepository userRepository){
        this.ownerProfileRepository = ownerProfileRepository;
        this.userRepository = userRepository;
    }

    public OwnerProfileDto getOwnerByUserId(Long userId){
        Optional<PetOwnerProfile> ownerProfileOpt = ownerProfileRepository.findOwnerProfileByUser_Id(userId);
        if(ownerProfileOpt.isPresent()){
            return OwnerProfileMapper.toOwnerProfileDto(ownerProfileOpt.get());
        }
        return null;
    }

    public OwnerProfileDto createOwnerProfile(User user) {
        PetOwnerProfile petOwnerProfile = new PetOwnerProfile()
                .setUser(user);
        return OwnerProfileMapper.toOwnerProfileDto(ownerProfileRepository.save(petOwnerProfile));
    }

}
