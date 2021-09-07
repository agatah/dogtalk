package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.dto.mappers.ServiceMapper;
import com.agatah.dogtalk.model.ServiceOffer;
import com.agatah.dogtalk.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceOfferService {

    private ServiceRepository serviceRepository;

    @Autowired
    public ServiceOfferService(ServiceRepository serviceRepository){
        this.serviceRepository = serviceRepository;
    }

    public ServiceDto getServiceById(Long id){
        Optional<ServiceOffer> serviceOpt = serviceRepository.findById(id);
        if(serviceOpt.isPresent()){
            return ServiceMapper.toServiceDto(serviceOpt.get());
        }
        return null;
    }

    public List<ServiceDto> getAllServicesBySchoolId(Long id){
        return serviceRepository.findAllBySchool_Id(id)
                .stream()
                .map(ServiceMapper::toServiceDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id){
        serviceRepository.deleteById(id);
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
            return null;
        }
    }
}
