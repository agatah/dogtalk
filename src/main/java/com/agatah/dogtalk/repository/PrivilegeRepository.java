package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    List<Privilege> findAllByBehaviorist_Id(Long id);
}
