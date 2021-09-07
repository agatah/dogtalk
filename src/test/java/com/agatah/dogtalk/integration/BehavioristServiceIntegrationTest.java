package com.agatah.dogtalk.integration;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.SchoolForm;
import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.model.Privilege;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.model.enums.RoleType;
import com.agatah.dogtalk.repository.*;
import com.agatah.dogtalk.service.BehavioristService;
import com.agatah.dogtalk.service.SchoolService;
import com.agatah.dogtalk.service.UserService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test")
public class BehavioristServiceIntegrationTest {

    @Autowired private BehavioristProfileRepository behavioristProfileRepository;
    @Autowired private SchoolRepository schoolRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;
    @Autowired private BehavioristService behavioristService;
    @Autowired private SchoolService schoolService;
    @Autowired private EntityManager em;
    @Autowired private UserService userService;

    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    void setUp() {
        userDto1 = userService.createNewUser(new UserDto()
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email1")
                .setPassword("password")
                .setRole(RoleType.ROLE_BEHAVIORIST));

        userDto2 = userService.createNewUser(new UserDto()
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email2")
                .setPassword("password")
                .setRole(RoleType.ROLE_BEHAVIORIST));

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        behavioristProfileRepository.deleteAll();
        schoolRepository.deleteAll();
        behavioristPrivilegesInSchoolRepository.deleteAll();
    }

    @Test
    @Transactional
    void itShouldAddNewSchoolWithDefaultPrivilege() {
        // when
        behavioristService.createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(schoolRepository.count())
                .as("number of schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("number of privileges in school")
                .isEqualTo(1);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("number of privileges in behaviorist")
                .isEqualTo(1);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getBehavioristPrivilegesInSchools().get(0).getPrivileges().get(0).getPrivilegeType())
                .as("privilege type")
                .isEqualTo(PrivilegeType.MANAGE);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldAddBehavioristToExistingSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        // when
        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(userDto2.getId(),
                        behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId(), Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)));

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(2);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privileges in repo")
                .isEqualTo(2);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("privileges in school")
                .isEqualTo(2);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteBehavioristAndSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));
        em.flush();

        // when
        behavioristProfileRepository.deleteById(userDto1.getId());
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeManage(behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorists in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privilege in repo")
                .isEqualTo(0);
        softly.assertThat(userRepository.count())
                .as("users in repo")
                .isEqualTo(2);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteOneBehavioristButNotSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(userDto2.getId(),
                        behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId(), Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)));
        em.flush();

        // when
        behavioristProfileRepository.deleteById(userDto2.getId());
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeManage(behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(1);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privileges in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("privileges in school")
                .isEqualTo(1);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteOneBehavioristAndSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(userDto2.getId(),
                        behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId(), Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)));
        em.flush();

        // when
        behavioristProfileRepository.deleteById(userDto1.getId());
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeManage(behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(1);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privileges in repo")
                .isEqualTo(0);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("privileges in survived behaviorist")
                .isEqualTo(0);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteSchoolButNotBehaviorists() {
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(userDto2.getId(),
                        behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId(), Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)));
        em.flush();

        // when
        schoolService.deleteSchoolById(behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(2);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privileges in repo")
                .isEqualTo(0);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("privileges in survived behaviorist1")
                .isEqualTo(0);
        softly.assertThat(behavioristProfileRepository.findAll().get(1).getBehavioristPrivilegesInSchools().size())
                .as("privileges in survived behaviorist2")
                .isEqualTo(0);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldRemoveBehavioristFromSchoolButNotDeleteIt() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(userDto2.getId(),
                        behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId(), Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)));
        em.flush();

        // when
        behavioristService.leaveSchool(behavioristDto2.getId(), behavioristDto2.getSchoolWithPrivilegesList().get(0).getSchoolId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(2);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privileges in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getBehavioristPrivilegesInSchools().size())
                .as("privileges in school")
                .isEqualTo(1);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldRemoveBehavioristFromSchoolAndDeleteIt() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(userDto1.getId(), new SchoolForm().setFormSchoolName("name"));

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(userDto2.getId(),
                        behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId(), Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)));
        em.flush();

        // when
        behavioristService.leaveSchool(behavioristDto1.getId(), behavioristDto1.getSchoolWithPrivilegesList().get(0).getSchoolId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(2);
        softly.assertThat(behavioristPrivilegesInSchoolRepository.count())
                .as("privileges in repo")
                .isEqualTo(0);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(userRepository.count())
                .as("users in repo")
                .isEqualTo(2);
        softly.assertAll();
    }
}
