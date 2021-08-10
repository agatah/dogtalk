package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.model.School;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchoolRepositoryTest {

    @Autowired private SchoolRepository underTest;

    @BeforeAll
    void beforeAll() {
        Location location1 = new Location().setCity("city1");
        Location location2 = new Location().setCity("city2");

        School school1 = new School()
                .setName("name")
                .addLocation(location1)
                .addLocation(location2);
        School school2 = new School()
                .setName("name")
                .addLocation(location1);
        School school3 = new School()
                .setName("name")
                .addLocation(location2);

        underTest.saveAll(Arrays.asList(school1, school2, school3));
    }

    @AfterAll
    void afterAll() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFind2SchoolsByCity1() {
        // when
        List<School> dbSchoolsList = underTest.findByLocationsCity("city1");

        // then
        assertThat(dbSchoolsList.size()).isEqualTo(2);
    }

    @Test
    void itShouldNotFindAnySchoolsByCity3() {
        // when
        List<School> dbSchoolsList = underTest.findByLocationsCity("city3");

        // then
        assertThat(dbSchoolsList.size()).isEqualTo(0);
    }
}