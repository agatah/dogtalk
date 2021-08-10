package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class PetProfileDto {

    private Long petId;
    private Long ownerId;
    private String name;
    private List<Long> photoIds = new ArrayList<>();
}
