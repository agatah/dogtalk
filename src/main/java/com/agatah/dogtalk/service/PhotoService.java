package com.agatah.dogtalk.service;

import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository){
        this.photoRepository = photoRepository;
    }

    public Photo uploadPhoto(Photo photo){
        return photoRepository.save(photo);
    }

    public Photo findById(Long id){
        Optional<Photo> photoOpt = photoRepository.findById(id);
        return photoOpt.orElseThrow(() -> new EntityNotFoundException(Photo.class, id));
    }


}
