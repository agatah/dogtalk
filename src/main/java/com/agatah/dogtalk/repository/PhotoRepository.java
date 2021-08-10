package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
