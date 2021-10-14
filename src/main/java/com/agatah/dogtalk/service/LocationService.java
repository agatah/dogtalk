package com.agatah.dogtalk.service;

import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.dto.mappers.LocationMapper;
import com.agatah.dogtalk.exception.EntityNotFoundException;
import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository){
        this.locationRepository = locationRepository;
    }

    public List<LocationDto> getAllLocations(){
        return locationRepository.findAll()
                .stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }

    public Location getLocationById(Long id){
        Optional<Location> locationOpt = locationRepository.findById(id);
        return locationOpt.orElseThrow(() -> new EntityNotFoundException(Location.class, id));

    }

    public Location getOrSaveLocationLikeDto(LocationDto locationDto){
        Optional<Location> locationOpt = locationRepository.findByCity(locationDto.getCity());
        return locationOpt.orElseGet(() -> locationRepository.save(LocationMapper.toLocation(locationDto)));
    }
}
