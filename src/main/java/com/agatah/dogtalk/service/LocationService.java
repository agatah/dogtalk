package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.dto.mappers.LocationMapper;
import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public List<LocationDto> getAllLocations(){
        return locationRepository.findAll()
                .stream()
                .map(location -> LocationMapper.toLocationDto(location))
                .collect(Collectors.toList());
    }

    public Location getLocationById(Long id){
        Optional<Location> locationOpt = locationRepository.findById(id);
        if(locationOpt.isPresent()){
            return locationOpt.get();
        }
        return null;
    }

}
