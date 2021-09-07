package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.*;
import com.agatah.dogtalk.dto.mappers.BehavioristProfileMapper;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.dto.mappers.LocationMapper;
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
import java.util.stream.Collectors;

@Service
public class BehavioristService {

    private final BehavioristProfileRepository behavioristProfileRepository;
    private final ContactRepository contactRepository;
    private final SchoolRepository schoolRepository;
    private final PrivilegeRepository privilegeRepository;
    private final SchoolService schoolService;
    private final LocationRepository locationRepository;
    private final BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;

    @Autowired
    public BehavioristService(BehavioristProfileRepository behavioristProfileRepository, ContactRepository contactRepository,
                              SchoolRepository schoolRepository, PrivilegeRepository privilegeRepository,
                              SchoolService schoolService, LocationRepository locationRepository,
                              BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository) {
        this.behavioristProfileRepository = behavioristProfileRepository;
        this.contactRepository = contactRepository;
        this.schoolRepository = schoolRepository;
        this.privilegeRepository = privilegeRepository;
        this.schoolService = schoolService;
        this.locationRepository = locationRepository;
        this.behavioristPrivilegesInSchoolRepository = behavioristPrivilegesInSchoolRepository;
    }

    public List<BehavioristFullProfileDto> getAllBehaviorists() {
        return behavioristProfileRepository.findAll()
                .stream()
                .map(BehavioristProfileMapper::toBehavioristFullProfileDto)
                .collect(Collectors.toList());
    }

    public List<BehavioristShortProfileDto> getAllBehavioristsShort() {
        return behavioristProfileRepository.findAll()
                .stream()
                .map(BehavioristProfileMapper::toBehavioristShortProfileDto)
                .collect(Collectors.toList());
    }

    public BehavioristFullProfileDto getBehavioristById(Long id){
        Optional<BehavioristProfile> behavioristDtoOpt = behavioristProfileRepository.findById(id);
        if(behavioristDtoOpt.isPresent()){
            return BehavioristProfileMapper
                    .toBehavioristFullProfileDto(behavioristDtoOpt.get());
        } else {
            return null;
        }
    }

    public BehavioristShortProfileDto getBehavioristShortById(Long id){
        Optional<BehavioristProfile> behavioristDtoOpt = behavioristProfileRepository.findById(id);
        if(behavioristDtoOpt.isPresent()){
            return BehavioristProfileMapper
                    .toBehavioristShortProfileDto(behavioristDtoOpt.get());
        } else {
            return null;
        }
    }


    public BehavioristProfile updateBehaviorist(BehavioristProfile behavioristProfile){
        return behavioristProfileRepository.save(behavioristProfile);
    }

    public BehavioristFullProfileDto updateBehavioristAbout(Long id, BehavioristFullProfileDto behaviorist){
        Optional<BehavioristProfile> dbBehavioristOpt = behavioristProfileRepository.findById(id);
        if(dbBehavioristOpt.isPresent()){
            BehavioristProfile dbBehaviorist = dbBehavioristOpt.get();
            dbBehaviorist
                    .setAbout(behaviorist.getAbout());
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(dbBehaviorist));
        } else {
            return null;
        }
    }

    public BehavioristFullProfileDto updateBehavioristQualifications(Long id, BehavioristFullProfileDto behaviorist){
        Optional<BehavioristProfile> dbBehavioristOpt = behavioristProfileRepository.findById(id);
        if(dbBehavioristOpt.isPresent()){
            BehavioristProfile dbBehaviorist = dbBehavioristOpt.get();
            dbBehaviorist
                    .setQualifications(behaviorist.getQualifications());
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(dbBehaviorist));
        } else {
            return null;
        }
    }

    public void deleteBehavioristContact(Long behavioristId, ContactDto contactDto){
        Optional<BehavioristProfile> behavioristProfileOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristProfileOpt.isPresent()){
            BehavioristProfile behavioristProfile = behavioristProfileOpt.get();
            Contact contact = ContactMapper.toContact(contactDto);

            behavioristProfile.removeContact(contact);
            behavioristProfileRepository.save(behavioristProfile);
            contactRepository.delete(contact);
        }
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
            return null;
        }
    }

    public void updateBehavioristContact(ContactDto contactDto){
        Contact contact = ContactMapper.toContact(contactDto);
        contactRepository.save(contact);
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
                Optional<Location> locationOpt = locationRepository.findByCity(locationDto.getCity());
                if(locationOpt.isPresent()){
                    school.addLocation(locationOpt.get());
                } else {
                    Location dbLocation = locationRepository.save(LocationMapper.toLocation(locationDto));
                    school.addLocation(dbLocation);
                }
            }
            behavioristOpt.get().addSchoolWithPrivileges(school, Arrays.asList(privilegeRepository.findPrivilegeByPrivilegeType(PrivilegeType.MANAGE)));
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(behavioristOpt.get()));
        }
        return null;
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
            return null;
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
            return null;
        }
    }

    public void removeBehavioristFromSchool(Long behavioristId, Long schoolId){
        Optional<BehavioristPrivilegesInSchool> behavioristPrivilegesInSchoolOpt = behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId);
        if(behavioristPrivilegesInSchoolOpt.isPresent()){
            BehavioristPrivilegesInSchool behavioristPrivilegesInSchool = behavioristPrivilegesInSchoolOpt.get();
            if(!behavioristPrivilegesInSchool.hasPrivilegeManage()){
                leaveSchool(behavioristId, schoolId);
            }
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
            return null;
        }
    }

    public void deleteById(Long userId){
        Optional<BehavioristProfile> behavioristProfileOpt = behavioristProfileRepository.findById(userId);
        if(behavioristProfileOpt.isPresent()){
            behavioristProfileRepository.deleteById(userId);
        }
    }
}
