package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.ServiceOffer;
import com.agatah.dogtalk.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ServiceOfferServiceTest {

    @Mock private ServiceRepository serviceRepository;
    private ServiceOfferService serviceOfferService;

    @BeforeEach
    void setUp() {
        serviceOfferService = new ServiceOfferService(serviceRepository);
    }

    @Test
    void shouldUpdateService() {
        // given
        ServiceDto serviceDto = new ServiceDto()
                .setServiceId(1L)
                .setDescription("d")
                .setServiceName("n")
                .setPrice("p");

        ServiceOffer serviceOffer = new ServiceOffer()
                .setId(1L)
                .setSchool(new School().setId(1L));

        given(serviceRepository.findById(serviceDto.getServiceId())).willReturn(Optional.of(serviceOffer));
        given(serviceRepository.save(serviceOffer)).will(invocation -> invocation.getArguments()[0]);

        // when
        ServiceDto savedServiceDto = serviceOfferService.updateService(serviceDto);

        // then
        verify(serviceRepository).findById(serviceDto.getServiceId());
        verify(serviceRepository).save(serviceOffer);
        assertThat(savedServiceDto.getServiceName()).isEqualTo(serviceDto.getServiceName());
        assertThat(savedServiceDto.getDescription()).isEqualTo(serviceDto.getDescription());
        assertThat(savedServiceDto.getPrice()).isEqualTo(serviceDto.getPrice());
    }

    @Test
    void shouldThrowExceptionWhenServiceNotFound() {
        // given
        ServiceDto serviceDto = new ServiceDto()
                .setServiceId(1L);

        given(serviceRepository.findById(serviceDto.getServiceId())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> serviceOfferService.updateService(serviceDto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ServiceOffer")
                .hasMessageContaining(String.valueOf(serviceDto.getServiceId()));

        verify(serviceRepository, times(0)).save(any(ServiceOffer.class));
    }
}