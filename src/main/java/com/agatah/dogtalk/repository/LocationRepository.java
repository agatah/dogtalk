package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCity(String city);

}
