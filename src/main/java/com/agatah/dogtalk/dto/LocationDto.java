package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LocationDto {

    private String city;

    public String toString(){
        return city;
    }
}
