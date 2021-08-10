package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.model.Contact;

public class ContactMapper {

    public static ContactDto toContactDto(Contact contact){
        return new ContactDto()
                .setContactId(contact.getId())
                .setContactType(contact.getContactType())
                .setValue(contact.getValue());
    }

    public static Contact toContact(ContactDto contactDto){
        return new Contact()
                .setId(contactDto.getContactId())
                .setContactType(contactDto.getContactType())
                .setValue(contactDto.getValue());
    }
}
