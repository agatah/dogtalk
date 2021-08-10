package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BehavioristShortProfileDto {

    private Long id;
    private String about;
    private String firstName;
    private String lastName;
    private Long photoId;

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
