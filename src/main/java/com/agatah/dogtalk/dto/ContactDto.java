package com.agatah.dogtalk.dto;

import com.agatah.dogtalk.model.enums.ContactType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertTrue;

@Data
@Accessors(chain = true)
public class ContactDto {

    private Long contactId;
    private ContactType contactType;
    private String value;

}
