package com.agatah.dogtalk.integration;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.BehavioristProfileRepository;
import com.agatah.dogtalk.repository.PrivilegeRepository;
import com.agatah.dogtalk.repository.SchoolRepository;
import com.agatah.dogtalk.repository.UserRepository;
import com.agatah.dogtalk.service.BehavioristService;
import com.agatah.dogtalk.service.SchoolService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class BehavioristServiceIntegrationTest {

    @Autowired private BehavioristProfileRepository behavioristProfileRepository;
    @Autowired private SchoolRepository schoolRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PrivilegeRepository privilegeRepository;
    @Autowired private BehavioristService behavioristService;
    @Autowired private SchoolService schoolService;
    @Autowired private EntityManager em;

    private User dbUser1;
    private User dbUser2;

    @BeforeEach
    void setUp() {
        User user1 = new User()
                .setFirstName("firstName")
                .setEmail("email")
                .setLastName("lastName")
                .setPassword("password")
                .setBehavioristProfile(new BehavioristProfile());

        User user2 = new User()
                .setFirstName("firstName")
                .setEmail("email2")
                .setLastName("lastName")
                .setPassword("password")
                .setBehavioristProfile(new BehavioristProfile());

        dbUser1 = userRepository.save(user1);
        dbUser2 = userRepository.save(user2);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        behavioristProfileRepository.deleteAll();
        schoolRepository.deleteAll();
        privilegeRepository.deleteAll();
    }

    @Test
    @Transactional
    void itShouldAddNewSchoolWithDefaultPrivilege() {
        // when
        behavioristService.createBehavioristSchool(dbUser1.getBehavioristProfile().getId(), "name");

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(schoolRepository.count())
                .as("number of schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getPrivileges().size())
                .as("number of privileges in school")
                .isEqualTo(1);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getPrivileges().size())
                .as("number of privileges in behaviorist")
                .isEqualTo(1);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getPrivileges().get(0).getPrivilegeType())
                .as("privilege type")
                .isEqualTo(PrivilegeType.ALL);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldAddBehavioristToExistingSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(dbUser1.getBehavioristProfile().getId(), "name");

        // when
        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(dbUser2.getBehavioristProfile().getId(),
                        behavioristDto1.getSchools().get(0).getId(), PrivilegeType.WRITE_MESSAGES);

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(2);
        softly.assertThat(privilegeRepository.count())
                .as("privileges in repo")
                .isEqualTo(2);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getPrivileges().size())
                .as("privileges in school")
                .isEqualTo(2);
        softly.assertAll();
    }

    @Test
    void itShouldDeleteBehavioristAndSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(dbUser1.getBehavioristProfile().getId(), "name");

        // when
        userRepository.deleteById(dbUser1.getId());
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeAll(behavioristDto1.getSchools().get(0).getId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorists in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(privilegeRepository.count())
                .as("privilege in repo")
                .isEqualTo(0);
        softly.assertThat(userRepository.count())
                .as("users in repo")
                .isEqualTo(1);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteOneBehavioristButNotSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(dbUser1.getBehavioristProfile().getId(), "name");

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(dbUser2.getBehavioristProfile().getId(),
                        behavioristDto1.getSchools().get(0).getId(), PrivilegeType.WRITE_MESSAGES);
        em.flush();

        // when
        userRepository.deleteById(dbUser2.getId());
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeAll(behavioristDto1.getSchools().get(0).getId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(1);
        softly.assertThat(privilegeRepository.count())
                .as("privileges in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(1);
        softly.assertThat(schoolRepository.findAll().get(0).getPrivileges().size())
                .as("privileges in school")
                .isEqualTo(1);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteOneBehavioristAndSchool() {
        // given
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(dbUser1.getBehavioristProfile().getId(), "name");

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(dbUser2.getBehavioristProfile().getId(),
                        behavioristDto1.getSchools().get(0).getId(), PrivilegeType.WRITE_MESSAGES);
        em.flush();

        // when
        userRepository.deleteById(dbUser1.getId());
        schoolService.deleteSchoolIfNoneBehavioristHasPrivilegeAll(behavioristDto1.getSchools().get(0).getId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(1);
        softly.assertThat(privilegeRepository.count())
                .as("privileges in repo")
                .isEqualTo(0);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getPrivileges().size())
                .as("privileges in survived behaviorist")
                .isEqualTo(0);
        softly.assertAll();
    }

    @Test
    @Transactional
    void itShouldDeleteSchoolButNotBehaviorists() {
        BehavioristFullProfileDto behavioristDto1 = behavioristService
                .createBehavioristSchool(dbUser1.getBehavioristProfile().getId(), "name");

        BehavioristFullProfileDto behavioristDto2 = behavioristService
                .addBehavioristToSchool(dbUser2.getBehavioristProfile().getId(),
                        behavioristDto1.getSchools().get(0).getId(), PrivilegeType.WRITE_MESSAGES);
        em.flush();

        // when
        schoolRepository.deleteById(behavioristDto1.getSchools().get(0).getId());

        // then
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(behavioristProfileRepository.count())
                .as("behaviorist in repo")
                .isEqualTo(2);
        softly.assertThat(privilegeRepository.count())
                .as("privileges in repo")
                .isEqualTo(0);
        softly.assertThat(schoolRepository.count())
                .as("schools in repo")
                .isEqualTo(0);
        softly.assertThat(behavioristProfileRepository.findAll().get(0).getPrivileges().size())
                .as("privileges in survived behaviorist1")
                .isEqualTo(0);
        softly.assertThat(behavioristProfileRepository.findAll().get(1).getPrivileges().size())
                .as("privileges in survived behaviorist2")
                .isEqualTo(0);
        softly.assertAll();
    }
}
