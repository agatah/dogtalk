package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @Mock private UserRepository userRepository;
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        userDetailService = new UserDetailService(userRepository);
    }

    @Test
    void shouldLoadUserByUsername() {
        // given
        User user = new User()
                .setEmail("email");
        given(userRepository.findUserByEmail(user.getEmail())).willReturn(Optional.of(user));

        // when
        UserDetails userDetails = userDetailService.loadUserByUsername(user.getEmail());

        // then
        verify(userRepository).findUserByEmail(user.getEmail());
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        User user = new User()
                .setEmail("email");
        given(userRepository.findUserByEmail(user.getEmail())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> userDetailService.loadUserByUsername(user.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(user.getEmail());

        verify(userRepository).findUserByEmail(user.getEmail());
    }
}