package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Role;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.model.enums.RoleType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    @Autowired private UserRepository underTest;
    @Autowired private RoleRepository roleRepository;

    @BeforeAll
    void beforeAll() {
        Role role = roleRepository.save(new Role().setRoleType(RoleType.ROLE_BEHAVIORIST));
        User user = new User()
                .setFirstName("firstName")
                .setEmail("email")
                .setLastName("lastName")
                .setPassword("password")
                .setRole(role);
        underTest.save(user);
    }

    @AfterAll
    void afterAll() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindUserByEmail() {
        // when
        Optional<User> userOpt = underTest.findUserByEmail("email");

        // then
        assertThat(userOpt.isPresent()).isEqualTo(true);
    }

    @Test
    void itShouldNotFindUserByEmail() {
        // when
        Optional<User> userOpt = underTest.findUserByEmail("email2");

        // then
        assertThat(userOpt.isPresent()).isEqualTo(false);
    }

    @Test
    void itShouldReturnTrueIfEmailAlreadyRegistered() {
        // when
        boolean expected = underTest.existsUserByEmail("email");

        // then
        assertThat(expected).isEqualTo(true);
    }

    @Test
    void itShouldReturnFalseIfEmailNotYetRegistered() {
        // when
        boolean expected = underTest.existsUserByEmail("email2");

        // then
        assertThat(expected).isEqualTo(false);
    }
}