package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.ServiceDto;
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
}
