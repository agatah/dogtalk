package com.agatah.dogtalk.repository;

import com.agatah.dogtalk.model.Privilege;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findPrivilegeByPrivilegeType(PrivilegeType privilegeType);
}
