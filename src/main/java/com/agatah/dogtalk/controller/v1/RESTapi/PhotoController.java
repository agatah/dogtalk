package com.agatah.dogtalk.controller.v1.RESTapi;

import com.agatah.dogtalk.service.PhotoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
public class PhotoController {

    PhotoService photoService;

    public PhotoController(PhotoService photoService){
        this.photoService = photoService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadPhoto(@PathVariable("id") Long id){
        return new ByteArrayResource(photoService.findById(id).getImage());
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getNumber(){
        return 1;
    }
}
