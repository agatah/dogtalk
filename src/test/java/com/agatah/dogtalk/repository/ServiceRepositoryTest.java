package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.ServiceOffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceRepositoryTest {

    @Autowired private ServiceRepository underTest;
    @Autowired private SchoolRepository schoolRepository;
    private School dbSchool;

    @BeforeAll
    void beforeAll() {
        School school = new School().setName("name")
                .addService(new ServiceOffer())
                .addService(new ServiceOffer());

        dbSchool = schoolRepository.save(school);
    }

    @AfterAll
    void afterAll() {
        schoolRepository.deleteAll();
    }

    @Test
    void itShouldFind2ServicesBySchoolId() {
        // when
        List<ServiceOffer> dbServicesList = underTest.findAllBySchool_Id(dbSchool.getId());

        // then
        assertThat(dbServicesList.size()).isEqualTo(2);
    }

    @Test
    void itShouldNotFindAnyServicesByWrongSchoolId() {
        // when
        List<ServiceOffer> dbServicesList = underTest.findAllBySchool_Id(dbSchool.getId()+1);

        // then
        assertThat(dbServicesList.size()).isEqualTo(0);
    }
}