package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.ServiceOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceOffer, Long> {

    List<ServiceOffer> findAllBySchool_Id(Long id);
}
