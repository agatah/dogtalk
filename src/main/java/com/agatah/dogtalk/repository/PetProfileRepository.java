package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetProfileRepository extends JpaRepository<Pet, Long> {
}
