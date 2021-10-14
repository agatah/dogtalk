package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Pet;
import com.agatah.dogtalk.model.PetOwnerProfile;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.repository.OwnerProfileRepository;
import com.agatah.dogtalk.repository.PetProfileRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock private PetProfileRepository petProfileRepository;
    @Mock private OwnerProfileRepository ownerProfileRepository;
    @Mock private PhotoService photoService;

    private PetService petService;

    @BeforeEach
    void setUp() {
        petService = new PetService(petProfileRepository, ownerProfileRepository, photoService);
    }

    @Test
    void shouldCreatePetProfileWhenOwnerFound() {
        // given
        Long petOwnerId = 1L;
        PetProfileDto petProfileDto = new PetProfileDto()
                .setPetName("name")
                .setAge("age")
                .setBreed("breed");

        PetOwnerProfile petOwnerProfile = new PetOwnerProfile()
                .setId(petOwnerId);
        given(ownerProfileRepository.findById(petOwnerId)).willReturn(Optional.of(petOwnerProfile));
        given(petProfileRepository.save(any(Pet.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);

        // when
        PetProfileDto returnedPetProfileDto = petService.createPetProfile(petOwnerId, petProfileDto);

        // then
        verify(petProfileRepository).save(petArgumentCaptor.capture());
        assertThat(petArgumentCaptor.getValue().getAge()).isEqualTo(petProfileDto.getAge());
        assertThat(petArgumentCaptor.getValue().getName()).isEqualTo(petProfileDto.getPetName());
        assertThat(petArgumentCaptor.getValue().getBreed()).isEqualTo(petProfileDto.getBreed());
        assertThat(petArgumentCaptor.getValue().getOwner().getId()).isEqualTo(petOwnerId);
        assertThat(returnedPetProfileDto.getPetName()).isEqualTo(petProfileDto.getPetName());
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        // given
        Long petOwnerId = 1L;
        PetProfileDto petProfileDto = new PetProfileDto()
                .setPetName("name")
                .setAge("age")
                .setBreed("breed");

        given(ownerProfileRepository.findById(petOwnerId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> petService.createPetProfile(petOwnerId, petProfileDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("PetOwnerProfile")
                .hasMessageContaining(String.valueOf(petOwnerId));
        verify(petProfileRepository, times(0)).save(any(Pet.class));

    }

    @Test
    void deletePetProfile() {
        // given
        Long id = 1L;
        given(petProfileRepository.existsById(id)).willReturn(true);

        // when
        petService.deletePetProfile(id);

        // then
        verify(petProfileRepository).deleteById(id);
    }

    @Test
    void shouldUpdatePetProfileWhenFound() {
        // given
        PetProfileDto petProfileDto = new PetProfileDto()
                .setPetId(1L)
                .setPetName("name")
                .setAge("age")
                .setBreed("breed");
        Pet pet = new Pet()
                .setOwner(new PetOwnerProfile());
        given(petProfileRepository.findById(petProfileDto.getPetId())).willReturn(Optional.of(pet));
        given(petProfileRepository.save(any(Pet.class))).will(i -> i.getArguments()[0]);
        ArgumentCaptor<Pet> petArgumentCaptor = ArgumentCaptor.forClass(Pet.class);

        // when
        PetProfileDto returnedPetProfileDto = petService.updatePetProfile(petProfileDto);

        // then
        verify(petProfileRepository).save(petArgumentCaptor.capture());
        assertThat(petArgumentCaptor.getValue().getName()).isEqualTo(petProfileDto.getPetName());
        assertThat(petArgumentCaptor.getValue().getAge()).isEqualTo(petProfileDto.getAge());
        assertThat(petArgumentCaptor.getValue().getBreed()).isEqualTo(petProfileDto.getBreed());
        assertThat(returnedPetProfileDto.getOwnerId()).isEqualTo(petProfileDto.getOwnerId());
    }

    @Test
    void shouldThrowExceptionWhenPetNotFound() {
        // given
        PetProfileDto petProfileDto = new PetProfileDto()
                .setPetId(1L);

        given(petProfileRepository.findById(petProfileDto.getPetId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> petService.updatePetProfile(petProfileDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pet")
                .hasMessageContaining(String.valueOf(petProfileDto.getPetId()));

        verify(petProfileRepository, times(0)).save(any(Pet.class));
    }

    @Test
    void shouldAddPhotoWhenPetFound() {
        // given
        Long petId = 1L;
        Pet pet = new Pet()
                .setId(petId)
                .setOwner(new PetOwnerProfile());
        Photo photo = new Photo()
                .setId(1L);
        given(petProfileRepository.findById(petId)).willReturn(Optional.of(pet));
        given(photoService.uploadPhoto(any(Photo.class))).will(i -> i.getArguments()[0]);
        given(petProfileRepository.save(any(Pet.class))).will(i -> i.getArguments()[0]);

        // when
        PetProfileDto returnedPetProfileDto = petService.addPhoto(petId, photo);

        // then
        verify(photoService).uploadPhoto(photo);
        verify(petProfileRepository).save(pet);
        assertThat(returnedPetProfileDto.getPhotoId()).isEqualTo(photo.getId());
    }

    @Test
    void shouldNotAddPhotoButThrowExceptionWhenPetNotFound() {
        // given
        Long petId = 1L;
        given(petProfileRepository.findById(petId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> petService.addPhoto(petId, any(Photo.class)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pet")
                .hasMessageContaining(String.valueOf(petId));
        verify(photoService, times(0)).uploadPhoto(any(Photo.class));
        verify(petProfileRepository, times(0)).save(any(Pet.class));
    }
}