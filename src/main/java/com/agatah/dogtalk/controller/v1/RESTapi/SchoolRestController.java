package com.agatah.dogtalk.controller.v1.RESTapi;

import com.agatah.dogtalk.dto.SchoolFullDto;
import com.agatah.dogtalk.dto.SchoolShortDto;
import com.agatah.dogtalk.model.BehavioristProfile;
import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.model.School;
import com.agatah.dogtalk.repository.LocationRepository;
import com.agatah.dogtalk.service.LocationService;
import com.agatah.dogtalk.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolRestController {

    private SchoolService schoolService;
    private LocationRepository locationService;

    @Autowired
    public SchoolRestController(SchoolService schoolService, LocationRepository locationService){
        this.schoolService = schoolService;
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolFullDto> getSchoolFullById(@PathVariable Long id){
        SchoolFullDto school = schoolService.getSchoolFullById(id);
        if(school != null){
            return new ResponseEntity<>(school, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = "city")
    public List<SchoolShortDto> getAllSchoolShortsByCity(@RequestParam(name = "city") String city){
        return schoolService.getAllSchoolShortsByCity(city);
    }

    @GetMapping
    public List<SchoolShortDto> getAllSchoolShorts(){
        return schoolService.getAllSchoolShorts();
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteSchool(@PathVariable Long id){
//        schoolService.deleteSchoolById(id);
//    }

//    @PostMapping
//    public void createSchool(){
//        schoolService.createSchool(new School()
//                .setName("333")
//                .setLocations(Arrays.asList(locationService.save(new Location().setCity("Smolec"))))
//                .addBehaviorist(new BehavioristProfile()));
//    }

}
