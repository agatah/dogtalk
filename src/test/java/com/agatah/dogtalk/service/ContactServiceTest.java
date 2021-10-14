package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Contact;
import com.agatah.dogtalk.model.enums.ContactType;
import com.agatah.dogtalk.repository.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock private ContactRepository contactRepository;
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        contactService = new ContactService(contactRepository);
    }

    @Test
    void shouldUpdateContactWhenFound() {
        // given
        ContactDto contactDto = new ContactDto()
                .setContactId(1L).setContactType(ContactType.PHONE)
                .setValue("123");
        Contact dbContact = new Contact();
        given(contactRepository.findById(contactDto.getContactId())).willReturn(Optional.of(dbContact));
        given(contactRepository.save(dbContact)).will(i -> i.getArguments()[0]);

        // when
        ContactDto savedContactDto = contactService.updateContact(contactDto);

        // then
        verify(contactRepository).save(dbContact);
        assertThat(savedContactDto.getValue()).isEqualTo(contactDto.getValue());
        assertThat(savedContactDto.getContactType()).isEqualTo(contactDto.getContactType());
    }

    @Test
    void shouldThrowExceptionWhenContactNotFound() {
        // given
        ContactDto contactDto = new ContactDto()
                .setContactId(1L).setContactType(ContactType.PHONE)
                .setValue("123");

        given(contactRepository.findById(contactDto.getContactId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> contactService.updateContact(contactDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Contact")
                .hasMessageContaining(String.valueOf(contactDto.getContactId()));

        verify(contactRepository, times(0)).save(any(Contact.class));
    }
}