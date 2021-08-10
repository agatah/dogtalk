package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.*;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.dto.mappers.SchoolMapper;
import com.agatah.dogtalk.dto.mappers.UserMapper;
import com.agatah.dogtalk.model.*;

import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.ContactRepository;
import com.agatah.dogtalk.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolService {

    private SchoolRepository schoolRepository;
    private BehavioristService behavioristService;
    private ContactRepository contactRepository;
    private PhotoService photoService;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository, BehavioristService behavioristService,
                         ContactRepository contactRepository, PhotoService photoService){
        this.schoolRepository = schoolRepository;
        this.behavioristService = behavioristService;
        this.contactRepository = contactRepository;
        this.photoService = photoService;
    }

    public SchoolFullDto getSchoolFullById(Long id){
        Optional<School> schoolOpt = schoolRepository.findById(id);
        if(schoolOpt.isPresent()){
            return SchoolMapper.toSchoolFullDto(schoolOpt.get());
        }
        return null;
    }

    public List<SchoolShortDto> getAllSchoolShortsByCity(String city){
        return schoolRepository.findByLocationsCity(city)
                .stream()
                .map(SchoolMapper::toSchoolShortDto)
                .collect(Collectors.toList());
    }

    public List<SchoolShortDto> getAllSchoolShorts(){
        return schoolRepository.findAll()
                .stream()
                .map(SchoolMapper::toSchoolShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSchoolIfNoneBehavioristHasPrivilegeAll(Long schoolId){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            List<Privilege> privileges = schoolOpt.get().getPrivileges();
            System.out.println("AAAAAAAAAAAAA");
            if(privileges.stream().noneMatch(s -> s.getPrivilegeType().equals(PrivilegeType.ALL))){
                System.out.println("NONE MATCH");
                schoolRepository.deleteById(schoolId);
            }
        }
    }

    public SchoolFullDto addSchoolContact(Long schoolId, ContactDto contact){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            School school = schoolOpt.get();
            school.addContact(new Contact()
                    .setContactType(contact.getContactType())
                    .setValue(contact.getValue()));
            return SchoolMapper.toSchoolFullDto(schoolRepository.save(school));
        } else {
            return null;
        }
    }

    public void deleteSchoolContact(Long schoolId, ContactDto contactDto){
        Contact contact = ContactMapper.toContact(contactDto);
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            School school = schoolOpt.get();
            schoolRepository.save(school.removeContact(contact));
        }
    }

    public SchoolFullDto deleteSchoolService(Long schoolId, ServiceDto service){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            School dbSchool = schoolOpt.get();
            dbSchool.removeService(new ServiceOffer()
                    .setSchool(dbSchool)
                    .setDescription(service.getDescription())
                    .setId(service.getServiceId())
                    .setPrice(service.getPrice())
                    .setName(service.getServiceName()));
            return SchoolMapper.toSchoolFullDto(schoolRepository.save(dbSchool));
        } else {
            return null;
        }
    }

    public SchoolFullDto addSchoolService(Long schoolId, ServiceDto service){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            School school = schoolOpt.get();
            school.addService(new ServiceOffer()
                    .setName(service.getServiceName())
                    .setDescription(service.getDescription())
                    .setPrice(service.getPrice()));
            return SchoolMapper.toSchoolFullDto(schoolRepository.save(school));
        } else {
            return null;
        }
    }

    public SchoolFullDto addPhoto(Long schoolId, Photo photo){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            return SchoolMapper.toSchoolFullDto(schoolRepository.save(schoolOpt.get().setBanner(photoService.uploadPhoto(photo))));
        }
        return null;
    }

//    public void deleteSchoolById(Long id){
//        School school = schoolRepository.findById(id).get();
//        for(Privilege privilege: new ArrayList<>(school.getPrivileges())){
//            privilege.removeSchool(school);
//        }
//        schoolRepository.delete(school);
//    }

//    public void createSchool(School school){
//        for(BehavioristProfile behaviorist: new ArrayList<>(school.getBehavioristProfiles())){
//            behavioristService.updateBehaviorist(behaviorist);
//        }
//    }

}