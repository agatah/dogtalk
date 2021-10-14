package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.OwnerProfileDto;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.repository.OwnerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock private OwnerProfileRepository ownerProfileRepository;
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        ownerService = new OwnerService(ownerProfileRepository);
    }

    @Test
    void shouldReturnOwnerProfileDtoWhenFound() {
        // given
        PetOwnerProfile petOwnerProfile = new PetOwnerProfile()
                .setId(1L)
                .setUser(new User());
        given(ownerProfileRepository.findById(petOwnerProfile.getId())).willReturn(Optional.of(petOwnerProfile));

        // when
        OwnerProfileDto ownerProfileDto = ownerService.getOwnerById(petOwnerProfile.getId());

        // then
        verify(ownerProfileRepository).findById(petOwnerProfile.getId());
        assertThat(ownerProfileDto.getOwnerId()).isEqualTo(petOwnerProfile.getId());
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        // given
        PetOwnerProfile petOwnerProfile = new PetOwnerProfile()
                .setId(1L)
                .setUser(new User());
        given(ownerProfileRepository.findById(petOwnerProfile.getId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> ownerService.getOwnerById(petOwnerProfile.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.valueOf(petOwnerProfile.getId()))
                .hasMessageContaining("PetOwnerProfile");
        verify(ownerProfileRepository).findById(petOwnerProfile.getId());
    }

    @Test
    void shouldCreateOwnerProfile() {
        // given
        User user = new User().setId(1L);
        ArgumentCaptor<PetOwnerProfile> petOwnerProfileArgumentCaptor = ArgumentCaptor.forClass(PetOwnerProfile.class);
        given(ownerProfileRepository.save(any(PetOwnerProfile.class))).will(i -> i.getArguments()[0]);

        // when
        OwnerProfileDto ownerProfileDto = ownerService.createOwnerProfile(user);

        // then
        verify(ownerProfileRepository).save(petOwnerProfileArgumentCaptor.capture());
        assertThat(petOwnerProfileArgumentCaptor.getValue().getUser().getId()).isEqualTo(user.getId());
        assertThat(ownerProfileDto.getUserId()).isEqualTo(user.getId());
    }

    @Test
    void shouldDeletePetOwnerByIdIfExists() {
        // given
        Long id = 1L;
        given(ownerProfileRepository.existsById(id)).willReturn(true);

        // when
        ownerService.deleteById(id);

        // then
        verify(ownerProfileRepository).deleteById(id);
    }
}