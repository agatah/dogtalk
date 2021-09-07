package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Role;
import com.agatah.dogtalk.model.enums.RoleType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoleRepositoryTest {

    @Autowired private RoleRepository underTest;

    @BeforeAll
    void setUp() {
        underTest.deleteAll();
        Role role1 = new Role().setRoleType(RoleType.ROLE_BEHAVIORIST);
        underTest.save(role1);
    }

    @AfterAll
    void afterAll() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindRoleByType() {
        // when
        Optional<Role> dbRoleOpt = underTest.findByRoleType(RoleType.ROLE_BEHAVIORIST);

        // then
        assertThat(dbRoleOpt.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindRoleByType() {
        // when
        Optional<Role> dbRoleOpt = underTest.findByRoleType(RoleType.ROLE_PET_OWNER);

        // then
        assertThat(dbRoleOpt.isPresent()).isFalse();
    }
}