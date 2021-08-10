package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class OwnerProfileRepositoryTest {

    @Autowired private OwnerProfileRepository underTest;
    @Autowired private UserRepository userRepository;

    private User dbUser;

    @BeforeAll
    void beforeAll() {
        PetOwnerProfile petOwner = new PetOwnerProfile();

        User user = new User()
                .setFirstName("firstName")
                .setEmail("email")
                .setLastName("lastName")
                .setPassword("password")
                .setPetOwnerProfile(petOwner);

        dbUser = userRepository.save(user);
    }

    @AfterAll
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    void itShouldFindOwnerProfileByUser_Id() {
        // when
        Optional<PetOwnerProfile> dbPetOwnerOpt = underTest.findOwnerProfileByUser_Id(dbUser.getId());

        // then
        assertThat(dbPetOwnerOpt.isPresent()).isEqualTo(true);
    }

    @Test
    void itShouldNotFindOwnerProfileByUser_Id() {
        // when
        Optional<PetOwnerProfile> dbPetOwnerOpt = underTest.findOwnerProfileByUser_Id(dbUser.getId()+1);

        // then
        assertThat(dbPetOwnerOpt.isPresent()).isEqualTo(false);
    }
}