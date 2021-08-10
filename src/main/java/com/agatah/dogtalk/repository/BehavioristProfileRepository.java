package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.BehavioristProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BehavioristProfileRepository extends JpaRepository<BehavioristProfile, Long> {

    Optional<BehavioristProfile> findBehavioristProfileByUser_Id(Long id);

}
