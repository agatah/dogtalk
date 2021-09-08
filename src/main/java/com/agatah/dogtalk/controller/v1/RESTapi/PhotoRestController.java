package com.agatah.dogtalk.controller.v1.RESTapi;

import com.agatah.dogtalk.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/image")
public class PhotoRestController {

    private final PhotoService photoService;

    @Autowired
    public PhotoRestController(PhotoService photoService){
        this.photoService = photoService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadPhoto(@PathVariable("id") Long id){
        return new ByteArrayResource(photoService.findById(id).getImage());
    }
}
