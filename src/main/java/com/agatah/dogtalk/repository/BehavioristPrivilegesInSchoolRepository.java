package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.BehavioristPrivilegesInSchool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BehavioristPrivilegesInSchoolRepository extends JpaRepository<BehavioristPrivilegesInSchool, Long> {

    List<BehavioristPrivilegesInSchool> findAllByBehaviorist_Id(Long id);
    Optional<BehavioristPrivilegesInSchool> findByBehaviorist_IdAndSchool_Id(Long behavioristId, Long schoolId);
}
