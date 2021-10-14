package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.SchoolFullDto;
import com.agatah.dogtalk.dto.SchoolShortDto;
import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.dto.mappers.SchoolMapper;
import com.agatah.dogtalk.dto.mappers.ServiceMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.*;
import com.agatah.dogtalk.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final PhotoService photoService;

    @Autowired
    public SchoolService(SchoolRepository schoolRepository, PhotoService photoService){
        this.schoolRepository = schoolRepository;
        this.photoService = photoService;
    }

    public SchoolFullDto getSchoolFullById(Long id){
        Optional<School> schoolOpt = schoolRepository.findById(id);
        return schoolOpt
                .map(SchoolMapper::toSchoolFullDto)
                .orElseThrow(() -> new EntityNotFoundException(School.class, id));

    }

    public Page<SchoolShortDto> getAllSchoolShortsByCity(String city, int page){
        return schoolRepository
                .findByLocationsCity(city, PageRequest.of(page, 5))
                .map(SchoolMapper::toSchoolShortDto);
    }

    public Page<SchoolShortDto> getAllSchoolShorts(int page){
        return schoolRepository
                .findAll(PageRequest.of(page, 5))
                .map(SchoolMapper::toSchoolShortDto);
    }

    @Transactional
    public void deleteSchoolIfNoneBehavioristHasPrivilegeManage(Long schoolId){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        if(schoolOpt.isPresent()){
            List<BehavioristPrivilegesInSchool> privilegesInSchool = schoolOpt.get().getBehavioristPrivilegesInSchools();
            if(privilegesInSchool.stream().noneMatch(BehavioristPrivilegesInSchool::hasPrivilegeManage)){
                schoolRepository.deleteById(schoolId);
            }
        }
    }

    public SchoolFullDto addSchoolContact(Long schoolId, ContactDto contact){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        return schoolOpt
                .map(school -> school.addContact(new Contact(contact.getContactType(), contact.getValue())))
                .map(schoolRepository::save)
                .map(SchoolMapper::toSchoolFullDto)
                .orElseThrow(() -> new EntityNotFoundException(School.class, schoolId));

    }

    public void deleteSchoolContact(Long schoolId, ContactDto contactDto){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        schoolOpt
                .map(school -> school.removeContact(ContactMapper.toContact(contactDto)))
                .map(schoolRepository::save)
                .orElseThrow(() -> new EntityNotFoundException(School.class, schoolId));
    }

    public SchoolFullDto deleteSchoolService(Long schoolId, ServiceDto serviceDto){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        return schoolOpt
                .map(school -> school.removeService(ServiceMapper.toServiceOffer(serviceDto, school)))
                .map(schoolRepository::save)
                .map(SchoolMapper::toSchoolFullDto)
                .orElseThrow(() -> new EntityNotFoundException(School.class, schoolId));
    }

    public SchoolFullDto addSchoolService(Long schoolId, ServiceDto serviceDto){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        return schoolOpt
                .map(school -> school.addService(ServiceMapper.toServiceOffer(serviceDto, school)))
                .map(schoolRepository::save)
                .map(SchoolMapper::toSchoolFullDto)
                .orElseThrow(() -> new EntityNotFoundException(School.class, schoolId));

    }

    public SchoolFullDto addPhoto(Long schoolId, Photo photo){
        Optional<School> schoolOpt = schoolRepository.findById(schoolId);
        return schoolOpt
                .map(school -> school.setBanner(photoService.uploadPhoto(photo)))
                .map(schoolRepository::save)
                .map(SchoolMapper::toSchoolFullDto)
                .orElseThrow(() -> new EntityNotFoundException(School.class, schoolId));

    }

    public void deleteSchoolById(Long id){
        if(schoolRepository.existsById(id)){
            schoolRepository.deleteById(id);
        }
    }
}
