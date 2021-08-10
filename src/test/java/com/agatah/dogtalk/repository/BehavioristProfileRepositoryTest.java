package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BehavioristProfileRepositoryTest {

    @Autowired private BehavioristProfileRepository underTest;
    @Autowired private UserRepository userRepository;

    private User dbUser;

    @BeforeAll
    void beforeAll() {
        User user = new User()
                .setFirstName("firstName")
                .setEmail("email")
                .setLastName("lastName")
                .setPassword("password")
                .setBehavioristProfile(new BehavioristProfile());

        dbUser = userRepository.save(user);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldFindBehavioristProfileByUser_Id() {
        // when
        Optional<BehavioristProfile> dbBehavioristOpt = underTest.findBehavioristProfileByUser_Id(dbUser.getId());

        // then
        assertThat(dbBehavioristOpt.isPresent()).isEqualTo(true);
    }

    @Test
    void itShouldNotFindBehavioristProfileByUser_Id() {
        // when
        Optional<BehavioristProfile> dbBehavioristOpt = underTest.findBehavioristProfileByUser_Id(dbUser.getId()+1);

        // then
        assertThat(dbBehavioristOpt.isPresent()).isEqualTo(false);
    }
}