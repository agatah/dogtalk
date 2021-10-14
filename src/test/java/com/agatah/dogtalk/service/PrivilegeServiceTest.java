package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.SchoolWithPrivilegesDto;
import com.agatah.dogtalk.model.BehavioristPrivilegesInSchool;
import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.Privilege;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.BehavioristPrivilegesInSchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PrivilegeServiceTest {

    @Mock private BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;
    private PrivilegeService privilegeService;

    @BeforeEach
    void setUp() {
        privilegeService = new PrivilegeService(behavioristPrivilegesInSchoolRepository);
    }

    @Test
    void shouldFindAllPrivilegesByBehavioristId() {
        // given
        Long behavioristId = 1L;
        School school = new School().setId(1L).setName("name");
        BehavioristPrivilegesInSchool behavioristPrivilegesInSchool = new BehavioristPrivilegesInSchool()
                .setId(1L)
                .setPrivileges(Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.RESPOND)))
                .setBehaviorist(new BehavioristProfile())
                .setSchool(school);

        given(behavioristPrivilegesInSchoolRepository.findAllByBehaviorist_Id(behavioristId))
                .willReturn(Arrays.asList(behavioristPrivilegesInSchool));

        // when
        List<SchoolWithPrivilegesDto> list = privilegeService.findAllByBehavioristId(behavioristId);

        // then
        verify(behavioristPrivilegesInSchoolRepository).findAllByBehaviorist_Id(behavioristId);
        assertThat(list.get(0).getSchoolName()).isEqualTo(school.getName());
    }
}