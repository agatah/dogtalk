package com.agatah.dogtalk.controller.v1.RESTapi;

import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
public class LocationRestController {

    private final LocationService locationService;

    @Autowired
    public LocationRestController(LocationService locationService){
        this.locationService = locationService;
    }

    @GetMapping
    public List<String> getLocations(@RequestParam(value = "term", required = false) String term){
        if(ObjectUtils.isEmpty(term)){
            return locationService.getAllLocations()
                    .stream()
                    .limit(10)
                    .map(LocationDto::toString)
                    .collect(Collectors.toList());
        }
        return locationService.getAllLocations()
                .stream()
                .map(LocationDto::toString)
                .filter(city -> city.toLowerCase().contains(term.toLowerCase()))
                .limit(10)
                .collect(Collectors.toList());
    }

}
