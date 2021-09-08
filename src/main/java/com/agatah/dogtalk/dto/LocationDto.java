package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class LocationDto {

    @NotBlank(message = "city should not not be blank")
    private String city;

    public String toString(){
        return city;
    }
}
