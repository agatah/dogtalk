package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Contact;
import com.agatah.dogtalk.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    public ContactDto updateContact(ContactDto contactDto){
        Optional<Contact> contactOpt =  contactRepository.findById(contactDto.getContactId());
        if(contactOpt.isPresent()){
            Contact dbContact = contactOpt.get();
            dbContact.setContactType(contactDto.getContactType())
                    .setValue(contactDto.getValue());
            return ContactMapper.toContactDto(contactRepository.save(dbContact));
        }
        throw new EntityNotFoundException(Contact.class, contactDto.getContactId());
    }
}
