package com.agatah.dogtalk.relations;

import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.BehavioristProfileRepository;
import com.agatah.dogtalk.repository.SchoolRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class BehavioristsSchoolsRelationTest {

//    @Autowired private BehavioristProfileRepository behavioristProfileRepository;
//    @Autowired private UserRepository userRepository;
//    @Autowired private SchoolRepository schoolRepository;
//
//    @Test
//    void itShouldPersistSchool(){
//        // given
//        User user = new User()
//                .setFirstName("firstName")
//                .setEmail("email")
//                .setLastName("lastName")
//                .setPassword("password")
//                .setBehavioristProfile(new BehavioristProfile());
//
//        User dbUser = userRepository.save(user);
//
//        BehavioristProfile dbBehaviorist = dbUser.getBehavioristProfile();
//
//        dbBehaviorist.addSchool(new School().setName("name"));
//
//        // when
//        behavioristProfileRepository.save(dbBehaviorist);
//        List<School> dbSchoolsList = schoolRepository.findAll();
//
//        // then
//        assertThat(dbSchoolsList.size()).isEqualTo(1);
//        assertThat(dbSchoolsList.get(0).getBehavioristProfiles().size()).isEqualTo(1);
//    }

}
