package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findAllByLocationsCity(String city);
    Page<School> findByLocationsCity(String city, Pageable page);
}
