package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.SchoolWithPrivilegesDto;
import com.agatah.dogtalk.dto.mappers.PrivilegeMapper;
import com.agatah.dogtalk.repository.BehavioristPrivilegesInSchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivilegeService {

    private final BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository;

    @Autowired
    public PrivilegeService(BehavioristPrivilegesInSchoolRepository behavioristPrivilegesInSchoolRepository){
        this.behavioristPrivilegesInSchoolRepository = behavioristPrivilegesInSchoolRepository;
    }

    public List<SchoolWithPrivilegesDto> findAllByBehavioristId(Long id){
        return behavioristPrivilegesInSchoolRepository.findAllByBehaviorist_Id(id)
                .stream()
                .map(PrivilegeMapper::toSchoolWithPrivilegesDto)
                .collect(Collectors.toList());
    }
}
