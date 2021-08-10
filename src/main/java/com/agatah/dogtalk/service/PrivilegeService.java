package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.PrivilegeDto;
import com.agatah.dogtalk.dto.mappers.PrivilegeMapper;
import com.agatah.dogtalk.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivilegeService {

    private PrivilegeRepository privilegeRepository;

    @Autowired
    public PrivilegeService(PrivilegeRepository privilegeRepository){
        this.privilegeRepository = privilegeRepository;
    }

    public List<PrivilegeDto> findAllByBehavioristId(Long id){
        return privilegeRepository.findAllByBehaviorist_Id(id)
                .stream()
                .map(PrivilegeMapper::toPrivilegeDto)
                .collect(Collectors.toList());
    }
}
