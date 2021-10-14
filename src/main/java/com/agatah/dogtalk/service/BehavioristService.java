package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.dto.SchoolForm;
import com.agatah.dogtalk.dto.mappers.BehavioristProfileMapper;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.dto.mappers.LocationMapper;
import com.agatah.dogtalk.exception.BehavioristPrivilegesInSchoolNotFoundException;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.*;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BehavioristService {

    private final BehavioristProfileRepository behavioristProfileRepository;
    private final ContactRepository contactRepository;
    private final SchoolRepository schoolRepository;
    private final PrivilegeRepository privilegeRepository;
    private final SchoolService schoolService;
    private final LocationRepository locationRepository;
    private final BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;
    private final ContactService contactService;
    private final LocationService locationService;

    @Autowired
    public BehavioristService(BehavioristProfileRepository behavioristProfileRepository, ContactRepository contactRepository,
                              SchoolRepository schoolRepository, PrivilegeRepository privilegeRepository,
                              SchoolService schoolService, LocationRepository locationRepository,
                              BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository,
                              ContactService contactService, LocationService locationService) {
        this.behavioristProfileRepository = behavioristProfileRepository;
        this.contactRepository = contactRepository;
        this.schoolRepository = schoolRepository;
        this.privilegeRepository = privilegeRepository;
        this.schoolService = schoolService;
        this.locationRepository = locationRepository;
        this.behavioristPrivilegesInSchoolRepository = behavioristPrivilegesInSchoolRepository;
        this.contactService = contactService;
        this.locationService = locationService;
    }

    public BehavioristFullProfileDto getBehavioristById(Long id){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(id);
        return behavioristOpt
                .map(BehavioristProfileMapper::toBehavioristFullProfileDto)
                .orElseThrow(() -> new EntityNotFoundException(BehavioristProfile.class, id));
    }

    public BehavioristFullProfileDto updateBehavioristAbout(Long id, BehavioristFullProfileDto behavioristDto){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(id);
        return behavioristOpt
                .map(behaviorist -> behaviorist.setAbout(behavioristDto.getAbout()))
                .map(behavioristProfileRepository::save)
                .map(BehavioristProfileMapper::toBehavioristFullProfileDto)
                .orElseThrow(() -> new EntityNotFoundException(BehavioristProfile.class, id));

    }

    public BehavioristFullProfileDto updateBehavioristQualifications(Long id, BehavioristFullProfileDto behavioristDto){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(id);
        return behavioristOpt
                .map(behaviorist -> behaviorist.setQualifications(behavioristDto.getQualifications()))
                .map(behavioristProfileRepository::save)
                .map(BehavioristProfileMapper::toBehavioristFullProfileDto)
                .orElseThrow(() -> new EntityNotFoundException(BehavioristProfile.class, id));
    }

    public void deleteBehavioristContact(Long behavioristId, ContactDto contactDto){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(behavioristId);
        behavioristOpt
                .map(behaviorist -> behaviorist.removeContact(ContactMapper.toContact(contactDto)))
                .map(behavioristProfileRepository::save)
                .orElseThrow(() -> new EntityNotFoundException(BehavioristProfile.class, behavioristId));
        contactRepository.delete(ContactMapper.toContact(contactDto));
    }

    public BehavioristFullProfileDto addBehavioristContact(Long  behavioristId, ContactDto contactDto){
        Optional<BehavioristProfile> behavioristProfileOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristProfileOpt.isPresent()){
            BehavioristProfile behavioristProfile = behavioristProfileOpt.get();
            Contact contact = ContactMapper.toContact(contactDto);

            behavioristProfile.addContact(contact);
            contactRepository.save(contact);
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(behavioristProfile));
        } else {
            throw new EntityNotFoundException(BehavioristProfile.class, behavioristId);
        }
    }

    public BehavioristFullProfileDto createBehavioristProfile(User user) {
        BehavioristProfile behavioristProfile = new BehavioristProfile().setUser(user);
        return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(behavioristProfile));
    }

    @Transactional
    public BehavioristFullProfileDto createBehavioristSchool(Long behavioristId, SchoolForm schoolForm){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristOpt.isPresent()){
            School school = new School().setName(schoolForm.getFormSchoolName());
            for(LocationDto locationDto: schoolForm.getLocations()){
                school.addLocation(locationService.getOrSaveLocationLikeDto(locationDto));
            }
            behavioristOpt.get().addSchoolWithPrivileges(school, Arrays.asList(privilegeRepository.findPrivilegeByPrivilegeType(PrivilegeType.MANAGE)));
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(behavioristOpt.get()));
        }
        throw new EntityNotFoundException(BehavioristProfile.class, behavioristId);
    }

    @Transactional
    public BehavioristFullProfileDto addBehavioristToSchool(Long behavioristId, Long schoolId, List<Privilege> privilegeDtoList) {
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristOpt.isPresent()){
            List<Privilege> dbPrivileges = new ArrayList<>();
            for(Privilege privilege: privilegeDtoList){
                dbPrivileges.add(privilegeRepository.findPrivilegeByPrivilegeType(privilege.getPrivilegeType()));
            }
            return BehavioristProfileMapper.toBehavioristFullProfileDto(
                    behavioristOpt.get().addSchoolWithPrivileges(schoolRepository.findById(schoolId).get(), dbPrivileges));
        } else {
            throw new EntityNotFoundException(BehavioristProfile.class, behavioristId);
        }
    }

    @Transactional
    public BehavioristFullProfileDto leaveSchool(Long behavioristId, Long schoolId){
        Optional<BehavioristProfile> behavioristProfileOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristProfileOpt.isPresent()){
            BehavioristProfile dbBehaviorist = behavioristProfileOpt.get();
            dbBehaviorist.removeSchoolWithPrivilege(schoolRepository.getById(schoolId));
            schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeManage(schoolId);
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(dbBehaviorist));
        } else {
            throw new EntityNotFoundException(BehavioristProfile.class, behavioristId);
        }
    }

    public BehavioristFullProfileDto removeBehavioristFromSchool(Long behavioristId, Long schoolId){
        Optional<BehavioristPrivilegesInSchool> behavioristPrivilegesInSchoolOpt = behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId);
        if(behavioristPrivilegesInSchoolOpt.isPresent()){
            BehavioristPrivilegesInSchool behavioristPrivilegesInSchool = behavioristPrivilegesInSchoolOpt.get();
            if(!behavioristPrivilegesInSchool.hasPrivilegeManage()){
                return leaveSchool(behavioristId, schoolId);
            }
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristPrivilegesInSchool.getBehaviorist());
        } else {
            throw new BehavioristPrivilegesInSchoolNotFoundException(behavioristId, schoolId);
        }
    }

    public BehavioristPrivilegesInSchool updateBehavioristPrivileges(Long schoolId, Long behavioristId, List<PrivilegeType> privileges){
        Optional<BehavioristPrivilegesInSchool> behavioristPrivilegesInSchoolOpt =
                behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId);
        if(behavioristPrivilegesInSchoolOpt.isPresent()){
            List<Privilege> dbPrivileges = new ArrayList<>();
            for(PrivilegeType privilegeType: privileges){
                dbPrivileges.add(privilegeRepository.findPrivilegeByPrivilegeType(privilegeType));
            }
            BehavioristPrivilegesInSchool dbBehavioristPrivilegesInSchool = behavioristPrivilegesInSchoolOpt.get();
            return behavioristPrivilegesInSchoolRepository.save(dbBehavioristPrivilegesInSchool.setPrivileges(dbPrivileges));
        } else {
            throw new BehavioristPrivilegesInSchoolNotFoundException(behavioristId, schoolId);
        }
    }

    public void deleteById(Long userId){
        if(behavioristProfileRepository.existsById(userId)){
            behavioristProfileRepository.deleteById(userId);
        }
    }
}
