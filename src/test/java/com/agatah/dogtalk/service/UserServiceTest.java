package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.model.enums.RoleType;
import com.agatah.dogtalk.repository.RoleRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private BehavioristService behavioristService;
    @Mock private OwnerService ownerService;
    @Mock private PhotoService photoService;
    @Mock private PasswordEncoder passwordEncoder;

    private UserService underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserService(userRepository, roleRepository, behavioristService, ownerService, photoService, passwordEncoder);
    }

    @Test
    void isEmailAlreadyRegistered() {
        // given
        String email = "email@gmail.com";
        UserDto userDto = new UserDto().setEmail(email);

        // when
        underTest.isEmailAlreadyRegistered(userDto);

        // then
        verify(userRepository).existsUserByEmail(userDto.getEmail());

    }

    @Test
    @Disabled
    void canCreateNewUser() {
        // given
        String email = "email@gmail.com";
        String firstName = "firstName";
        UserDto user = new UserDto()
                .setFirstName(firstName)
                .setEmail(email)
                .setPassword("password")
                .setLastName("lastName")
                .setRole(RoleType.ROLE_BEHAVIORIST);


        // when
        UserDto expected = underTest.createNewUser(user);

        // then
        assertThat(user.getFirstName()).isEqualTo(expected.getFirstName());
    }

    @Test
    @Disabled
    void canFindUserById() {
    }

    @Test
    @Disabled
    void updateUser() {
    }

    @Test
    @Disabled
    void updatePassword() {
    }

    @Test
    @Disabled
    void addPhoto() {
    }
}