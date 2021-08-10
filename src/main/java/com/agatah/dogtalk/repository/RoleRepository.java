package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByName(String name);
}
