package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.ServiceOffer;

public class ServiceMapper {

    public static ServiceDto toServiceDto(ServiceOffer serviceOffer){
        return new ServiceDto()
                .setServiceId(serviceOffer.getId())
                .setServiceName(serviceOffer.getName())
                .setDescription(serviceOffer.getDescription())
                .setPrice(serviceOffer.getPrice())
                .setSchoolId(serviceOffer.getSchool().getId());
    }

    public static ServiceOffer toServiceOffer(ServiceDto serviceDto, School school){
        return new ServiceOffer()
                .setSchool(school)
                .setDescription(serviceDto.getDescription())
                .setId(serviceDto.getServiceId())
                .setPrice(serviceDto.getPrice())
                .setName(serviceDto.getServiceName());
    }
}
