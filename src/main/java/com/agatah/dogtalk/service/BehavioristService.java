package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.BehavioristShortProfileDto;
import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.SchoolShortDto;
import com.agatah.dogtalk.dto.mappers.BehavioristProfileMapper;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.Contact;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.BehavioristProfileRepository;
import com.agatah.dogtalk.repository.ContactRepository;
import com.agatah.dogtalk.repository.SchoolRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BehavioristService {

    private BehavioristProfileRepository behavioristProfileRepository;
    private ContactRepository contactRepository;
    private UserRepository userRepository;
    private SchoolRepository schoolRepository;

    public BehavioristService(BehavioristProfileRepository behavioristProfileRepository, ContactRepository contactRepository,
                              UserRepository userRepository, SchoolRepository schoolRepository){
        this.behavioristProfileRepository = behavioristProfileRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
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

    public BehavioristFullProfileDto getBehavioristByUserId(Long id){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findBehavioristProfileByUser_Id(id);
        if(behavioristOpt.isPresent()){
            return BehavioristProfileMapper
                    .toBehavioristFullProfileDto(behavioristOpt.get());
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

    public BehavioristFullProfileDto updateBehavioristInfo(Long id, BehavioristFullProfileDto behaviorist){
        Optional<BehavioristProfile> dbBehavioristOpt = behavioristProfileRepository.findById(id);
        if(dbBehavioristOpt.isPresent()){
            BehavioristProfile dbBehaviorist = dbBehavioristOpt.get();
            dbBehaviorist
                    .setAbout(behaviorist.getAbout())
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
    public BehavioristFullProfileDto createBehavioristSchool(Long behavioristId, String schoolName){
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristOpt.isPresent()){
            School school = new School().setName(schoolName);
            behavioristOpt.get().addSchoolWithPrivilege(school, PrivilegeType.ALL);
            return BehavioristProfileMapper.toBehavioristFullProfileDto(behavioristProfileRepository.save(behavioristOpt.get()));
        }
        return null;
    }

    @Transactional
    public BehavioristFullProfileDto addBehavioristToSchool(Long behavioristId, Long schoolId, PrivilegeType privilege) {
        Optional<BehavioristProfile> behavioristOpt = behavioristProfileRepository.findById(behavioristId);
        if(behavioristOpt.isPresent()){
            return BehavioristProfileMapper.toBehavioristFullProfileDto(
                    behavioristOpt.get().addSchoolWithPrivilege(schoolRepository.findById(schoolId).get(), privilege));
        } else {
            return null;
        }
    }
}
