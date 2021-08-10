package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {

    List<School> findByLocationsCity(String city);
}
