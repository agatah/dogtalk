package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.PetOwnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerProfileRepository extends JpaRepository<PetOwnerProfile, Long> {

}
