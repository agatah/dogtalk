package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.dto.mappers.LocationMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock private LocationRepository locationRepository;
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        locationService = new LocationService(locationRepository);
    }

    @Test
    void shouldReturnListOfAllLocationsDto() {
        // given
        Location location = new Location();
        given(locationRepository.findAll()).willReturn(Arrays.asList(location));

        // when
        List<LocationDto> list = locationService.getAllLocations();

        // then
        verify(locationRepository).findAll();
        assertThat(list.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnLocationWhenFound() {
        // given
        Location dbLocation = new Location()
                .setId(1L)
                .setCity("city");
        given(locationRepository.findById(dbLocation.getId())).willReturn(Optional.of(dbLocation));

        // when
        Location location = locationService.getLocationById(dbLocation.getId());

        // then
        verify(locationRepository).findById(dbLocation.getId());
        assertThat(location.getCity()).isEqualTo(dbLocation.getCity());
    }

    @Test
    void shouldThrowExceptionWhenLocationNotFound() {
        // given
        Location dbLocation = new Location()
                .setId(1L)
                .setCity("city");
        given(locationRepository.findById(dbLocation.getId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> locationService.getLocationById(dbLocation.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Location")
                .hasMessageContaining(String.valueOf(dbLocation.getId()));

        verify(locationRepository).findById(dbLocation.getId());
    }

    @Test
    void shouldReturnLocationWhenFoundByCity() {
        // given
        LocationDto locationDto = new LocationDto().setCity("city");
        given(locationRepository.findByCity(locationDto.getCity())).willReturn(Optional.of(LocationMapper.toLocation(locationDto)));

        // when
        Location returnedLocation = locationService.getOrSaveLocationLikeDto(locationDto);

        // then
        verify(locationRepository, times(0)).save(any(Location.class));
        assertThat(returnedLocation.getCity()).isEqualTo(locationDto.getCity());
    }

    @Test
    void shouldSaveAndReturnNewLocationWhenNotFoundByCity() {
        // given
        LocationDto locationDto = new LocationDto().setCity("city");
        given(locationRepository.findByCity(locationDto.getCity())).willReturn(Optional.empty());
        given(locationRepository.save(any(Location.class))).will(i -> i.getArguments()[0]);

        // when
        Location returnedLocation = locationService.getOrSaveLocationLikeDto(locationDto);

        // then
        verify(locationRepository).save(any(Location.class));
        assertThat(returnedLocation.getCity()).isEqualTo(locationDto.getCity());
    }
}