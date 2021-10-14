package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.SchoolFullDto;
import com.agatah.dogtalk.dto.SchoolShortDto;
import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.dto.mappers.ServiceMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.*;
import com.agatah.dogtalk.model.enums.ContactType;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.SchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock private SchoolRepository schoolRepository;
    @Mock private PhotoService photoService;

    private SchoolService schoolService;

    @BeforeEach
    void setUp() {
        schoolService = new SchoolService(schoolRepository, photoService);
    }

    @Test
    void shouldReturnSchoolDtoWhenFound() {
        // given
        Long id = 1L;
        School school = new School().setId(id);
        given(schoolRepository.findById(id)).willReturn(Optional.of(school));

        // when
        SchoolFullDto returnedSchoolDto = schoolService.getSchoolFullById(id);

        // then
        assertThat(returnedSchoolDto.getId()).isEqualTo(school.getId());
    }

    @Test
    void shouldThrowExceptionWhenSchoolNotFound() {
        // given
        Long id = 1L;
        given(schoolRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> schoolService.getSchoolFullById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("School")
                .hasMessageContaining(String.valueOf(id));
    }

    @Test
    void shouldReturnPageOfSchoolShortsByCity() {
        // given
        School school = new School()
                .setId(1L);
        given(schoolRepository.findByLocationsCity(anyString(), any(PageRequest.class)))
                .willReturn(new PageImpl<>(Arrays.asList(school)));

        // when
        Page<SchoolShortDto> schoolShortDtoPage = schoolService.getAllSchoolShortsByCity(anyString(), anyInt());

        // then
        verify(schoolRepository).findByLocationsCity(anyString(), any(PageRequest.class));
        assertThat(schoolShortDtoPage.getContent().get(0).getId()).isEqualTo(school.getId());
    }

    @Test
    void shouldReturnPageOfSchoolShorts() {
        // given
        School school = new School()
                .setId(1L);
        given(schoolRepository.findAll(any(PageRequest.class)))
                .willReturn(new PageImpl<>(Arrays.asList(school)));

        // when
        Page<SchoolShortDto> schoolShortDtoPage = schoolService.getAllSchoolShorts(anyInt());

        // then
        verify(schoolRepository).findAll(any(PageRequest.class));
        assertThat(schoolShortDtoPage.getContent().get(0).getId()).isEqualTo(school.getId());
    }

    @Test
    void shouldDeleteSchoolIfNoneBehavioristHasPrivilegeManage() {
        // given
        Long schoolId = 1L;
        School school = new School()
                .addPrivilege(new BehavioristPrivilegesInSchool()
                        .setPrivileges(Arrays.asList(new Privilege(PrivilegeType.RESPOND)))
                        .setBehaviorist(new BehavioristProfile()));
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));

        // when
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeManage(schoolId);

        // then
        verify(schoolRepository).deleteById(schoolId);
    }

    @Test
    void shouldNotDeleteSchoolIfOneBehavioristHasPrivilegeManage() {
        // given
        Long schoolId = 1L;
        School school = new School()
                .addPrivilege(new BehavioristPrivilegesInSchool()
                        .setPrivileges(Arrays.asList(new Privilege(PrivilegeType.MANAGE)))
                        .setBehaviorist(new BehavioristProfile()));
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));

        // when
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeManage(schoolId);

        // then
        verify(schoolRepository, times(0)).deleteById(schoolId);
    }

    @Test
    void shouldAddContactWhenSchoolFound() {
        // given
        Long schoolId = 1L;
        ContactDto contactDto = new ContactDto()
                .setValue("value")
                .setContactType(ContactType.SITE);
        School school = new School();
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));
        given(schoolRepository.save(any(School.class))).will(i -> i.getArguments()[0]);

        // when
        SchoolFullDto returnedSchoolDto = schoolService.addSchoolContact(schoolId, contactDto);

        // then
        verify(schoolRepository).save(school);
        assertThat(returnedSchoolDto.getContacts().get(0).getContactType()).isEqualTo(contactDto.getContactType());
        assertThat(returnedSchoolDto.getContacts().get(0).getValue()).isEqualTo(contactDto.getValue());
    }

    @Test
    void shouldNotAddContactButThrowExceptionWhenSchoolNotFound() {
        // given
        Long schoolId = 1L;
        given(schoolRepository.findById(schoolId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> schoolService.addSchoolContact(schoolId, any(ContactDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("School")
                .hasMessageContaining(String.valueOf(schoolId));
        verify(schoolRepository, times(0)).save(any(School.class));
    }

    @Test
    void shouldDeleteContactWhenSchoolFound() {
        // given
        Long schoolId = 1L;
        ContactDto contactDto = new ContactDto()
                .setContactType(ContactType.SITE)
                .setValue("value")
                .setContactId(1L);
        School school = new School()
                .setId(schoolId)
                .addContact(ContactMapper.toContact(contactDto));
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));
        given(schoolRepository.save(any(School.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<School> schoolArgumentCaptor = ArgumentCaptor.forClass(School.class);

        // when
        schoolService.deleteSchoolContact(schoolId, contactDto);

        // then
        verify(schoolRepository).save(schoolArgumentCaptor.capture());
        assertThat(schoolArgumentCaptor.getValue().getContacts().size()).isEqualTo(0);
    }

    @Test
    void shouldNotDeleteContactButThrowExceptionWhenSchoolNotFound() {
        // given
        Long schoolId = 1L;
        given(schoolRepository.findById(anyLong())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> schoolService.deleteSchoolContact(schoolId, any(ContactDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("School")
                .hasMessageContaining(String.valueOf(schoolId));
        verify(schoolRepository, times(0)).save(any(School.class));
    }

    @Test
    void shouldDeleteServiceWhenSchoolFound() {
        // given
        Long schoolId = 1L;
        ServiceDto serviceDto = new ServiceDto()
                .setServiceId(1L)
                .setDescription("description")
                .setServiceName("name")
                .setPrice("price");
        School school = new School().setId(schoolId);
        school.addService(ServiceMapper.toServiceOffer(serviceDto, school));
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));
        given(schoolRepository.save(any(School.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<School> schoolArgumentCaptor = ArgumentCaptor.forClass(School.class);

        // when
        SchoolFullDto returnedSchoolDto = schoolService.deleteSchoolService(schoolId, serviceDto);

        // then
        verify(schoolRepository).save(schoolArgumentCaptor.capture());
        assertThat(schoolArgumentCaptor.getValue().getServiceOffers().size()).isEqualTo(0);
        assertThat(returnedSchoolDto.getId()).isEqualTo(school.getId());
    }

    @Test
    void shouldNotDeleteServiceButThrowExceptionWhenSchoolNotFound() {
        // given
        Long schoolId = 1L;
        given(schoolRepository.findById(schoolId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> schoolService.deleteSchoolService(schoolId, any(ServiceDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("School")
                .hasMessageContaining(String.valueOf(schoolId));
        verify(schoolRepository, times(0)).save(any(School.class));
    }

    @Test
    void shouldAddSchoolServiceWhenSchoolFound() {
        // given
        Long schoolId = 1L;
        ServiceDto serviceDto = new ServiceDto()
                .setDescription("description")
                .setServiceName("name")
                .setPrice("price");
        School school = new School().setId(schoolId);

        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));
        given(schoolRepository.save(any(School.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<School> schoolArgumentCaptor = ArgumentCaptor.forClass(School.class);

        // when
        SchoolFullDto returnedSchoolDto = schoolService.addSchoolService(schoolId, serviceDto);

        // then
        verify(schoolRepository).save(schoolArgumentCaptor.capture());
        assertThat(schoolArgumentCaptor.getValue().getServiceOffers().size()).isEqualTo(1);
        assertThat(returnedSchoolDto.getId()).isEqualTo(school.getId());
    }

    @Test
    void shouldNotAddSchoolServiceButThrowExceptionWhenSchoolNotFound() {
        // given
        Long schoolId = 1L;
        given(schoolRepository.findById(schoolId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> schoolService.addSchoolService(schoolId, any(ServiceDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("School")
                .hasMessageContaining(String.valueOf(schoolId));
        verify(schoolRepository, times(0)).save(any(School.class));
    }

    @Test
    void shouldAddPhotoWhenSchoolFound() {
        // given
        Long schoolId = 1L;
        School school = new School().setId(schoolId);
        Photo photo = new Photo().setId(1L);
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));
        given(photoService.uploadPhoto(any(Photo.class))).will(i -> i.getArguments()[0]);
        given(schoolRepository.save(any(School.class))).will(i -> i.getArguments()[0]);

        // when
        SchoolFullDto returnedSchoolDto = schoolService.addPhoto(schoolId, photo);

        // then
        verify(schoolRepository).save(school);
        verify(photoService).uploadPhoto(photo);
        assertThat(returnedSchoolDto.getBannerId()).isEqualTo(photo.getId());
    }

    @Test
    void shouldNotAddPhotoButThrowExceptionWhenSchoolNotFound() {
        // given
        Long schoolId = 1L;
        given(schoolRepository.findById(schoolId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> schoolService.addPhoto(schoolId, any(Photo.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("School")
                .hasMessageContaining(String.valueOf(schoolId));
        verify(schoolRepository, times(0)).save(any(School.class));
        verify(photoService, times(0)).uploadPhoto(any(Photo.class));
    }

    @Test
    void shouldDeleteSchoolById() {
        // given
        Long id = 1L;
        given(schoolRepository.existsById(id)).willReturn(true);

        // when
        schoolService.deleteSchoolById(id);

        // then
        verify(schoolRepository).deleteById(id);
    }
}