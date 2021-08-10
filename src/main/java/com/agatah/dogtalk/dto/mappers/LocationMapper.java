package com.agatah.dogtalk.dto.mappers;

import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.model.Location;

public class LocationMapper {

    public static LocationDto toLocationDto(Location location){
        return new LocationDto()
                .setCity(location.getCity());
    }

    public static Location toLocation(LocationDto locationDto){
        return new Location()
                .setCity(locationDto.getCity());
    }
}
