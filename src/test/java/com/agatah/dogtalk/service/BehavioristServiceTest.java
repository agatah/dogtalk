package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.dto.SchoolForm;
import com.agatah.dogtalk.dto.mappers.ContactMapper;
import com.agatah.dogtalk.dto.mappers.LocationMapper;
import com.agatah.dogtalk.exception.BehavioristPrivilegesInSchoolNotFoundException;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.*;
import com.agatah.dogtalk.model.enums.ContactType;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BehavioristServiceTest {

    @Mock private BehavioristProfileRepository behavioristProfileRepository;
    @Mock private ContactRepository contactRepository;
    @Mock private SchoolRepository schoolRepository;
    @Mock private PrivilegeRepository privilegeRepository;
    @Mock private SchoolService schoolService;
    @Mock private LocationRepository locationRepository;
    @Mock private BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;
    @Mock private ContactService contactService;
    @Mock private LocationService locationService;

    private BehavioristService behavioristService;

    @BeforeEach
    void setUp() {
        behavioristService = new BehavioristService(behavioristProfileRepository, contactRepository, schoolRepository,
                privilegeRepository, schoolService, locationRepository, behavioristPrivilegesInSchoolRepository,
                contactService, locationService);
    }

    @Test
    void shouldReturnBehavioristDtoWhenFoundById() {
        // given
        Long id = 1L;
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(id)
                .setUser(new User());
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.of(behavioristProfile));

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.getBehavioristById(id);

        // then
        verify(behavioristProfileRepository).findById(id);
        assertThat(returnedBehavioristDto.getId()).isEqualTo(behavioristProfile.getId());
    }

    @Test
    void shouldThrowExceptionWhenBehavioristNotFoundById() {
        // given
        Long id = 1L;
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.getBehavioristById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(id));
    }

    @Test
    void shouldUpdateAboutWhenBehavioristFound() {
        // given
        Long id = 1L;
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(id)
                .setUser(new User());
        BehavioristFullProfileDto behavioristDto = new BehavioristFullProfileDto()
                .setAbout("about");
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.of(behavioristProfile));
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.updateBehavioristAbout(id, behavioristDto);

        // then
        verify(behavioristProfileRepository).findById(id);
        verify(behavioristProfileRepository).save(behavioristProfile);
        assertThat(returnedBehavioristDto.getAbout()).isEqualTo(behavioristDto.getAbout());
    }

    @Test
    void shouldNotUpdateAboutButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long id = 1L;
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.updateBehavioristAbout(id, any(BehavioristFullProfileDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(id));
    }

    @Test
    void shouldUpdateQualificationsWhenBehavioristFound() {
        // given
        Long id = 1L;
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(id)
                .setUser(new User());
        BehavioristFullProfileDto behavioristDto = new BehavioristFullProfileDto()
                .setQualifications("qualifications");
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.of(behavioristProfile));
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.updateBehavioristQualifications(id, behavioristDto);

        // then
        verify(behavioristProfileRepository).findById(id);
        verify(behavioristProfileRepository).save(behavioristProfile);
        assertThat(returnedBehavioristDto.getQualifications()).isEqualTo(behavioristDto.getQualifications());
    }

    @Test
    void shouldNotUpdateQualificationsButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long id = 1L;
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.updateBehavioristQualifications(id, any(BehavioristFullProfileDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(id));
    }

    @Test
    void shouldDeleteContactWhenBehavioristFound() {
        // given
        Long id = 1L;
        ContactDto contactDto = new ContactDto()
                .setContactId(1L)
                .setContactType(ContactType.SITE)
                .setValue("value");
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(id)
                .setUser(new User())
                .addContact(ContactMapper.toContact(contactDto));
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.of(behavioristProfile));
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<BehavioristProfile> behavioristArgumentCaptor = ArgumentCaptor.forClass(BehavioristProfile.class);

        // when
        behavioristService.deleteBehavioristContact(id, contactDto);

        // then
        verify(behavioristProfileRepository).save(behavioristArgumentCaptor.capture());
        assertThat(behavioristArgumentCaptor.getValue().getContactList().size()).isEqualTo(0);
        verify(contactRepository).delete(ContactMapper.toContact(contactDto));

    }

    @Test
    void shouldNotDeleteContactButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long id = 1L;
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.deleteBehavioristContact(id, any(ContactDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(id));
        verify(contactRepository, times(0)).delete(any(Contact.class));
    }

    @Test
    void shouldAddContactWhenBehavioristFound() {
        // given
        Long id = 1L;
        ContactDto contactDto = new ContactDto()
                .setContactId(1L)
                .setContactType(ContactType.SITE)
                .setValue("value");
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(id)
                .setUser(new User());
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.of(behavioristProfile));
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.addBehavioristContact(id, contactDto);

        // then
        verify(behavioristProfileRepository).save(behavioristProfile);
        verify(contactRepository).save(ContactMapper.toContact(contactDto));
        assertThat(returnedBehavioristDto.getContacts().size()).isEqualTo(1);
    }

    @Test
    void shouldNotAddContactButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long id = 1L;
        given(behavioristProfileRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.addBehavioristContact(id, any(ContactDto.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(id));
        verify(contactRepository, times(0)).save(any(Contact.class));
        verify(behavioristProfileRepository, times(0)).save(any(BehavioristProfile.class));
    }

    @Test
    void shouldCreateBehavioristProfile() {
        // given
        User user = new User()
                .setId(1L);
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<BehavioristProfile> behavioristArgumentCaptor = ArgumentCaptor.forClass(BehavioristProfile.class);

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.createBehavioristProfile(user);

        // then
        verify(behavioristProfileRepository).save(behavioristArgumentCaptor.capture());
        assertThat(behavioristArgumentCaptor.getValue().getUser().getId()).isEqualTo(user.getId());
        assertThat(returnedBehavioristDto).isNotNull();
    }

    @Test
    void shouldCreateSchoolWhenBehavioristFound() {
        // given
        Long behavioristId = 1L;
        LocationDto locationDto = new LocationDto().setCity("city");
        SchoolForm schoolForm = new SchoolForm()
                .setFormSchoolName("name")
                .setLocations(Arrays.asList(locationDto));
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setUser(new User());
        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.of(behavioristProfile));
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);
        given(privilegeRepository.findPrivilegeByPrivilegeType(PrivilegeType.MANAGE)).willReturn(new Privilege(PrivilegeType.MANAGE));
        given(locationService.getOrSaveLocationLikeDto(locationDto)).willReturn(LocationMapper.toLocation(locationDto));

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.createBehavioristSchool(behavioristId, schoolForm);

        // then
        verify(behavioristProfileRepository).save(behavioristProfile);
        assertThat(returnedBehavioristDto.getSchoolWithPrivilegesList().get(0).getSchoolName()).isEqualTo(schoolForm.getFormSchoolName());
        assertThat(returnedBehavioristDto.getSchoolWithPrivilegesList().get(0).getPrivileges().get(0)).isEqualTo(PrivilegeType.MANAGE);

    }

    @Test
    void shouldNotCreateSchoolButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long behavioristId = 1L;
        SchoolForm schoolForm = new SchoolForm()
                .setFormSchoolName("name")
                .setLocations(Arrays.asList(new LocationDto().setCity("city")));
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setUser(new User());
        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.createBehavioristSchool(behavioristId, any(SchoolForm.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(behavioristId));

        verify(locationRepository, times(0)).findByCity(anyString());
        verify(locationRepository, times(0)).save(any(Location.class));
        verify(behavioristProfileRepository, times(0)).save(behavioristProfile);
        verify(privilegeRepository, times(0)).findPrivilegeByPrivilegeType(any(PrivilegeType.class));
    }

    @Test
    void shouldAddBehavioristToSchoolWhenFound() {
        // given
        Long behavioristId = 1L;
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(behavioristId)
                .setUser(new User());
        Long schoolId = 1L;
        School school = new School().setId(schoolId);
        List<Privilege> privileges = new ArrayList<>();
        privileges.add(new Privilege(PrivilegeType.MANAGE));
        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.of(behavioristProfile));
        given(privilegeRepository.findPrivilegeByPrivilegeType(PrivilegeType.MANAGE)).willReturn(new Privilege(PrivilegeType.MANAGE));
        given(schoolRepository.findById(schoolId)).willReturn(Optional.of(school));

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.addBehavioristToSchool(behavioristId, schoolId, privileges);

        // then
        verify(privilegeRepository, times(privileges.size())).findPrivilegeByPrivilegeType(any(PrivilegeType.class));
        verify(schoolRepository).findById(schoolId);
        assertThat(returnedBehavioristDto.getSchoolWithPrivilegesList().size()).isEqualTo(1);
    }

    @Test
    void shouldNotAddBehavioristToSchoolButThrowException() {
        // given
        Long behavioristId = 1L;
        Long schoolId = 1L;
        List<Privilege> privileges = new ArrayList<>();
        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.addBehavioristToSchool(behavioristId, schoolId, privileges))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(behavioristId));

        verify(privilegeRepository, times(0)).findPrivilegeByPrivilegeType(any(PrivilegeType.class));
        verify(schoolRepository, times(0)).findById(anyLong());

    }

    @Test
    void shouldLeaveSchoolWhenBehavioristFound() {
        // given
        Long behavioristId = 1L;
        Long schoolId = 1L;
        School school = new School().setId(schoolId);
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(behavioristId)
                .setUser(new User());
        List<BehavioristPrivilegesInSchool> privileges = new ArrayList<>();
        privileges.add(new BehavioristPrivilegesInSchool()
                .setSchool(school)
                .setPrivileges(Arrays.asList(new Privilege(PrivilegeType.MANAGE)))
                .setBehaviorist(behavioristProfile));
        behavioristProfile.setBehavioristPrivilegesInSchools(privileges);

        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.of(behavioristProfile));
        given(schoolRepository.getById(schoolId)).willReturn(school);
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.leaveSchool(behavioristId, schoolId);

        // then
        verify(schoolService).deleteSchoolIfNoneBehavioristHasPrivilegeManage(schoolId);
        assertThat(returnedBehavioristDto.getSchoolWithPrivilegesList().size()).isEqualTo(0);
    }

    @Test
    void shouldNotLeaveSchoolButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long behavioristId = 1L;
        Long schoolId = 1L;
        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.leaveSchool(behavioristId, schoolId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("BehavioristProfile")
                .hasMessageContaining(String.valueOf(behavioristId));
        verify(behavioristProfileRepository, times(0)).save(any(BehavioristProfile.class));
    }

    @Test
    void shouldRemoveBehavioristFromSchoolWhenBehavioristFoundAndDontHavePrivilegeManage() {
        // given
        Long behavioristId = 1L;
        Long schoolId = 1L;
        School school = new School().setId(schoolId);
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(behavioristId)
                .setUser(new User());
        List<BehavioristPrivilegesInSchool> privileges = new ArrayList<>();
        BehavioristPrivilegesInSchool behavioristPrivilegesInSchool = new BehavioristPrivilegesInSchool()
                .setSchool(school)
                .setPrivileges(Arrays.asList(new Privilege(PrivilegeType.EDIT)))
                .setBehaviorist(behavioristProfile);
        privileges.add(behavioristPrivilegesInSchool);
        behavioristProfile.setBehavioristPrivilegesInSchools(privileges);

        given(behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId))
                .willReturn(Optional.of(behavioristPrivilegesInSchool));
        given(behavioristProfileRepository.findById(behavioristId)).willReturn(Optional.of(behavioristProfile));
        given(schoolRepository.getById(schoolId)).willReturn(school);
        given(behavioristProfileRepository.save(any(BehavioristProfile.class))).will(i -> i.getArguments()[0]);

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.removeBehavioristFromSchool(behavioristId, schoolId);

        // then
        assertThat(returnedBehavioristDto.getSchoolWithPrivilegesList().size()).isEqualTo(0);

    }

    @Test
    void shouldNotRemoveBehavioristFromSchoolWhenBehavioristFoundAndHavePrivilegeManage() {
        // given
        Long behavioristId = 1L;
        Long schoolId = 1L;
        School school = new School().setId(schoolId);
        BehavioristProfile behavioristProfile = new BehavioristProfile()
                .setId(behavioristId)
                .setUser(new User());
        List<BehavioristPrivilegesInSchool> privileges = new ArrayList<>();
        BehavioristPrivilegesInSchool behavioristPrivilegesInSchool = new BehavioristPrivilegesInSchool()
                .setSchool(school)
                .setPrivileges(Arrays.asList(new Privilege(PrivilegeType.MANAGE)))
                .setBehaviorist(behavioristProfile);
        privileges.add(behavioristPrivilegesInSchool);
        behavioristProfile.setBehavioristPrivilegesInSchools(privileges);

        given(behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId))
                .willReturn(Optional.of(behavioristPrivilegesInSchool));

        // when
        BehavioristFullProfileDto returnedBehavioristDto = behavioristService.removeBehavioristFromSchool(behavioristId, schoolId);

        // then
        assertThat(returnedBehavioristDto.getSchoolWithPrivilegesList().size()).isEqualTo(1);

    }

    @Test
    void shouldNotRemoveBehavioristFromSchoolButThrowExceptionWhenBehavioristNotFound() {
        // given
        Long behavioristId = 1L;
        Long schoolId = 1L;
        given(behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.removeBehavioristFromSchool(behavioristId, schoolId))
                .isInstanceOf(BehavioristPrivilegesInSchoolNotFoundException.class)
                .hasMessageContaining("behaviorist id: " + behavioristId)
                .hasMessageContaining("school id: " + schoolId);

    }

    @Test
    void shouldUpdateBehavioristPrivilegesWhenFound() {
        // given
        Long schoolId = 1L;
        School school = new School().setId(schoolId);
        Long behavioristId = 1L;
        BehavioristProfile behaviorist = new BehavioristProfile()
                .setId(behavioristId)
                .setUser(new User());
        List<Privilege> privilegesOld = Arrays.asList(new Privilege(PrivilegeType.RESPOND));
        List<PrivilegeType> privilegesNew = Arrays.asList(PrivilegeType.EDIT);
        BehavioristPrivilegesInSchool dbPrivileges = new BehavioristPrivilegesInSchool()
                .setId(1L)
                .setBehaviorist(behaviorist)
                .setSchool(school)
                .setPrivileges(privilegesOld);
        given(behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId))
                .willReturn(Optional.of(dbPrivileges));
        given(privilegeRepository.findPrivilegeByPrivilegeType(any(PrivilegeType.class)))
                .will(i -> new Privilege((PrivilegeType) i.getArguments()[0]));
        given(behavioristPrivilegesInSchoolRepository.save(any(BehavioristPrivilegesInSchool.class)))
                .will(i -> i.getArguments()[0]);

        // when
        BehavioristPrivilegesInSchool returnedPrivilegesDto = behavioristService
                .updateBehavioristPrivileges(schoolId, behavioristId, privilegesNew);

        // then
        assertThat(returnedPrivilegesDto.getPrivileges().get(0).getPrivilegeType()).isEqualTo(privilegesNew.get(0));
    }

    @Test
    void shouldNotUpdateBehavioristPrivilegesButThrowExceptionWhenNotFound() {
        // given
        Long schoolId = 1L;
        Long behavioristId = 2L;
        List<PrivilegeType> privilegesNew = Arrays.asList(PrivilegeType.EDIT);
        given(behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(behavioristId, schoolId))
                .willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> behavioristService.updateBehavioristPrivileges(schoolId, behavioristId, privilegesNew))
                .isInstanceOf(BehavioristPrivilegesInSchoolNotFoundException.class)
                .hasMessageContaining("behaviorist id: " + behavioristId)
                .hasMessageContaining("school id: " + schoolId);

    }

    @Test
    void shouldDeleteBehavioristById() {
        // given
        Long id= 1L;
        given(behavioristProfileRepository.existsById(id)).willReturn(true);

        // when
        behavioristService.deleteById(id);

        // then
        verify(behavioristProfileRepository).deleteById(id);
    }
}