package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.model.Role;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.model.enums.RoleType;
import com.agatah.dogtalk.repository.RoleRepository;
import com.agatah.dogtalk.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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

    private UserService userService;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleRepository, behavioristService,
                ownerService, photoService, passwordEncoder);
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        // given
        UserDto userDto = new UserDto().setEmail("email");
        given(userRepository.existsUserByEmail(userDto.getEmail())).willReturn(true);

        // when
        boolean exists = userService.isEmailAlreadyRegistered(userDto);

        // then
        verify(userRepository).existsUserByEmail(userDto.getEmail());
        assertThat(exists).isEqualTo(true);
    }

    @Test
    void shouldReturnFalseWhenEmailNotExists() {
        // given
        UserDto userDto = new UserDto().setEmail("email");
        given(userRepository.existsUserByEmail(userDto.getEmail())).willReturn(false);

        // when
        boolean exists = userService.isEmailAlreadyRegistered(userDto);

        // then
        verify(userRepository).existsUserByEmail(userDto.getEmail());
        assertThat(exists).isEqualTo(false);
    }

    @Test
    void shouldReturnUserDtoWhenUserFound() {
        // given
        User user = new User()
                .setId(1L)
                .setFirstName("firstName");
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        UserDto userDto = userService.findUserById(user.getId());

        // then
        verify(userRepository).findById(user.getId());
        assertThat(userDto.getId()).isEqualTo(user.getId());

    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        Long id = 1L;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // then

        assertThatThrownBy(() -> userService.findUserById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

    }

    @Test
    void shouldUpdatePasswordWhenUserFound() {
        // given
        UserDto userDto = new UserDto().setId(1L).setPassword("password");
        User dbUser = new User();
        given(userRepository.findById(userDto.getId())).willReturn(Optional.of(dbUser));

        // when
        userService.updatePassword(userDto.getId(), userDto);

        // then
        verify(userRepository).findById(userDto.getId());
        verify(passwordEncoder).encode(userDto.getPassword());
        verify(userRepository).save(dbUser);

    }

    @Test
    void shouldNotUpdatePasswordButThrowExceptionWhenUserNotFound() {
        // given
        UserDto userDto = new UserDto().setId(1L).setPassword("password");
        User dbUser = new User();
        given(userRepository.findById(userDto.getId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.updatePassword(userDto.getId(), userDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining(String.valueOf(userDto.getId()));

        verify(userRepository).findById(userDto.getId());
        verify(passwordEncoder, times(0)).encode(userDto.getPassword());
        verify(userRepository, times(0)).save(dbUser);

    }

    @Test
    void shouldAddPhotoWhenUserFound() {
        // given
        User user = new User()
                .setId(1L)
                .setFirstName("firstName");
        Photo photo = new Photo();
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        // when
        UserDto userDto = userService.addPhoto(user.getId(), photo);

        // then
        verify(userRepository).findById(user.getId());
        verify(photoService).uploadPhoto(photo);
        verify(userRepository).save(user);
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());

    }

    @Test
    void shouldNotAddPhotoButThrowExceptionWhenUserNotFound() {
        // given
        User user = new User()
                .setId(1L)
                .setFirstName("firstName");
        Photo photo = new Photo();
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.addPhoto(user.getId(), photo))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining(String.valueOf(user.getId()));

        // then
        verify(userRepository).findById(user.getId());
        verify(photoService, times(0)).uploadPhoto(photo);
        verify(userRepository, times(0)).save(user);

    }

    @Test
    void shouldDeleteUserBehavioristWhenFound() {
        // given
        Long id = 1L;
        User user = new User()
                .setId(id)
                .setRole(new Role().setRoleType(RoleType.ROLE_BEHAVIORIST));
        given(userRepository.findById(id)).willReturn(Optional.of(user));


        // when
        userService.deleteUserById(id);

        // then
        verify(behavioristService).deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldDeleteUserOwnerWhenFound() {
        // given
        Long id = 1L;
        User user = new User()
                .setId(id)
                .setRole(new Role().setRoleType(RoleType.ROLE_PET_OWNER));
        given(userRepository.findById(id)).willReturn(Optional.of(user));


        // when
        userService.deleteUserById(id);

        // then
        verify(ownerService).deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    void shouldUpdateUsersFirstAndLastNameWhenFound() {
        // given
        Long id = 1L;
        UserDto userDto = new UserDto()
                .setId(id)
                .setFirstName("firstName2")
                .setLastName("lastName2");
        User dbUser = new User()
                .setId(id)
                .setFirstName("firstName1")
                .setLastName("lastName1");

        given(userRepository.findById(id)).willReturn(Optional.of(dbUser));
        given(userRepository.save(dbUser)).willReturn(dbUser);

        // when
        UserDto savedUserDto = userService.updateUser(id, userDto);

        // then
        verify(userRepository).findById(id);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(capturedUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(savedUserDto.getFirstName()).isEqualTo(dbUser.getFirstName());
    }

    @Test
    void shouldNotUpdateUserButThrowExceptionWhenNotFound() {
        // given
        Long id = 1L;
        UserDto userDto = new UserDto()
                .setId(id)
                .setFirstName("firstName2")
                .setLastName("lastName2");
        User dbUser = new User()
                .setId(id)
                .setFirstName("firstName1")
                .setLastName("lastName1");

        given(userRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userService.updateUser(id, userDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining(String.valueOf(id));

        verify(userRepository).findById(id);
        verify(userRepository, times(0)).save(dbUser);
    }

    @Test
    void shouldCreateNewUserBehaviorist() {
        // given
        UserDto userDto = new UserDto()
                .setPassword("password")
                .setRole(RoleType.ROLE_BEHAVIORIST);
        User user = new User();
        given(roleRepository.findByRoleType(RoleType.ROLE_BEHAVIORIST))
                .willReturn(Optional.of(new Role(RoleType.ROLE_BEHAVIORIST)));
        given(userRepository.save(user)).willReturn(user);

        // when
        userService.createNewUser(userDto);

        // then
        verify(passwordEncoder).encode(userDto.getPassword());
        verify(roleRepository).findByRoleType(userDto.getRole());
        verify(userRepository).save(user);
        verify(behavioristService).createBehavioristProfile(user);

    }

    @Test
    void shouldCreateNewUserPetOwner() {
        // given
        UserDto userDto = new UserDto()
                .setPassword("password")
                .setRole(RoleType.ROLE_PET_OWNER);
        User user = new User();
        given(roleRepository.findByRoleType(RoleType.ROLE_PET_OWNER))
                .willReturn(Optional.of(new Role(RoleType.ROLE_PET_OWNER)));
        given(userRepository.save(user)).willReturn(user);

        // when
        userService.createNewUser(userDto);

        // then
        verify(passwordEncoder).encode(userDto.getPassword());
        verify(roleRepository).findByRoleType(userDto.getRole());
        verify(userRepository).save(user);
        verify(ownerService).createOwnerProfile(user);

    }

    @Test
    void shouldCreateUserWithSameValuesAsDto() {
        // given
        UserDto userDto = new UserDto()
                .setFirstName("firstName")
                .setLastName("lastName")
                .setEmail("email")
                .setRole(RoleType.ROLE_BEHAVIORIST);
        User user = new User();
        given(roleRepository.findByRoleType(any())).willReturn(Optional.of(new Role(userDto.getRole())));
        given(userRepository.save(any())).will(invocation -> invocation.getArguments()[0]);

        // when
        UserDto createdUser = userService.createNewUser(userDto);

        // then
        assertThat(createdUser.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(createdUser.getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void shouldSaveRoleTypeIfNotFound() {
        // given
        UserDto userDto = new UserDto()
                .setPassword("password")
                .setRole(RoleType.ROLE_PET_OWNER);
        User user = new User();
        given(roleRepository.findByRoleType(userDto.getRole())).willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        // when
        userService.createNewUser(userDto);

        // then
        verify(roleRepository).findByRoleType(userDto.getRole());

        ArgumentCaptor<Role> roleArgumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleArgumentCaptor.capture());

        Role captured = roleArgumentCaptor.getValue();
        assertThat(userDto.getRole()).isEqualTo(captured.getRoleType());
    }
}