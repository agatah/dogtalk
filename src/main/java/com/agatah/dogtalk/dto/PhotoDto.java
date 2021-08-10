package com.agatah.dogtalk.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PhotoDto {

    private Long photoId;
    private byte[] image;
}
