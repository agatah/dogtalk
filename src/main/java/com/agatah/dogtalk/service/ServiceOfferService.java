package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.dto.mappers.ServiceMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.ServiceOffer;
import com.agatah.dogtalk.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceOfferService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceOfferService(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    public ServiceDto updateService(ServiceDto serviceDto){
        Optional<ServiceOffer> serviceOpt = serviceRepository.findById(serviceDto.getServiceId());
        if(serviceOpt.isPresent()){
            ServiceOffer dbServiceOffer = serviceOpt.get();
            dbServiceOffer
                    .setDescription(serviceDto.getDescription())
                    .setName(serviceDto.getServiceName())
                    .setPrice(serviceDto.getPrice());
            return ServiceMapper.toServiceDto(serviceRepository.save(dbServiceOffer));

        } else {
            throw new EntityNotFoundException(ServiceOffer.class, serviceDto.getServiceId());
        }
    }
}
