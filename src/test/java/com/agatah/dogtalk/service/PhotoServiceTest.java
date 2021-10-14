package com.agatah.dogtalk.service;

import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.repository.PhotoRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

    @Mock private PhotoRepository photoRepository;
    private PhotoService photoService;

    @BeforeEach
    void setUp() {
        photoService = new PhotoService(photoRepository);
    }

    @Test
    void shouldUploadPhoto() {
        // given
        Photo photo = new Photo();
        ArgumentCaptor<Photo> photoArgumentCaptor = ArgumentCaptor.forClass(Photo.class);
        given(photoRepository.save(photo)).will(i -> i.getArguments()[0]);

        // when
        Photo savedPhoto = photoService.uploadPhoto(photo);

        // then
        verify(photoRepository).save(photoArgumentCaptor.capture());
        assertThat(savedPhoto).isEqualTo(photoArgumentCaptor.getValue());
    }

    @Test
    void shouldReturnPhotoWhenFound() {
        // given
        Photo photo = new Photo().setId(1L);
        given(photoRepository.findById(photo.getId())).willReturn(Optional.of(photo));

        // when
        Photo dbPhoto = photoService.findById(photo.getId());

        // then
        assertThat(dbPhoto.getId()).isEqualTo(photo.getId());
    }

    @Test
    void shouldThrowExceptionWhenPhotoNotFound() {
        // given
        Photo photo = new Photo().setId(1L);
        given(photoRepository.findById(photo.getId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> photoService.findById(photo.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Photo")
                .hasMessageContaining(String.valueOf(photo.getId()));
    }
}