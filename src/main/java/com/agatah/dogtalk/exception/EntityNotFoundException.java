package com.agatah.dogtalk.exception;

import com.agatah.dogtalk.model.User;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(Class<?> entityType, Long id){
        super("Entity of type: " + entityType.getSimpleName() + ", id: " + id + " not found");
    }
}
