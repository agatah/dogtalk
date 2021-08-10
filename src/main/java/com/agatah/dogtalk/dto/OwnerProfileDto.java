package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class OwnerProfileDto {

    private Long ownerId;
    private Long userId;
    private List<PetProfileDto> pets = new ArrayList<>();
}
