package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleRepositoryTest {

    @Autowired private RoleRepository underTest;

    @BeforeAll
    void setUp() {
        Role role1 = new Role().setName("ROLE_USER");
        Role role2 = new Role().setName("ROLE_BEHAVIORIST");

        underTest.saveAll(Arrays.asList(role1, role2));
    }

    @AfterAll
    void afterAll() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindRoleByName() {
        // given
        String roleUser = "ROLE_USER";

        // when
        Optional<Role> dbRoleOpt = underTest.findByName(roleUser);

        // then
        assertThat(dbRoleOpt.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindRoleByName() {
        // given
        String roleUser = "ROLE_ADMIN";

        // when
        Optional<Role> dbRoleOpt = underTest.findByName(roleUser);

        // then
        assertThat(dbRoleOpt.isPresent()).isFalse();
    }
}