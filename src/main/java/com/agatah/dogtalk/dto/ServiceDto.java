package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServiceDto {

    private Long serviceId;
    private String serviceName;
    private String description;
    private String price;
    private Long schoolId;
}
