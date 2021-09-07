package com.agatah.dogtalk.security;

import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.model.BehavioristPrivilegesInSchool;
import com.agatah.dogtalk.model.Privilege;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.model.User;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.repository.BehavioristPrivilegesInSchoolRepository;
import com.agatah.dogtalk.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Component("permissionEvaluator")
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    @Autowired
    private BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication != null && permission instanceof String) {

            UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
            String privilegeToCheck = (String) permission;
            School school = (School) targetDomainObject;

            Optional<BehavioristPrivilegesInSchool> behavioristPrivilegesInSchoolOpt =
                    behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(userDetails.getUserId(), school.getId());
            if(behavioristPrivilegesInSchoolOpt.isPresent()){
                List<Privilege> privileges = behavioristPrivilegesInSchoolOpt.get().getPrivileges();

                return privileges
                        .stream()
                        .map(Privilege::getPrivilegeType)
                        .map(PrivilegeType::toString)
                        .anyMatch(s -> s.equals("MANAGE") || s.equals(privilegeToCheck));
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication != null && permission instanceof String) {

            UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
            String privilegeToCheck = (String) permission;
            Long schoolId = (Long) targetId;

            Optional<BehavioristPrivilegesInSchool> behavioristPrivilegesInSchoolOpt =
                    behavioristPrivilegesInSchoolRepository.findByBehaviorist_IdAndSchool_Id(userDetails.getUserId(), schoolId);
            if(behavioristPrivilegesInSchoolOpt.isPresent()){
                List<Privilege> privileges = behavioristPrivilegesInSchoolOpt.get().getPrivileges();

                return privileges
                        .stream()
                        .map(Privilege::getPrivilegeType)
                        .map(PrivilegeType::toString)
                        .anyMatch(s -> s.equals("MANAGE") || s.equals(privilegeToCheck));
            }
        }
        return false;
    }
}
