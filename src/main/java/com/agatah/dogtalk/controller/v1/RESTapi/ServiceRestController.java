package com.agatah.dogtalk.controller.v1.RESTapi;

import com.agatah.dogtalk.dto.ServiceDto;
import com.agatah.dogtalk.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceRestController {

    private ServiceService serviceService;

    @Autowired
    public ServiceRestController(ServiceService serviceService){
        this.serviceService = serviceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id){
        ServiceDto serviceDto = serviceService.getServiceById(id);
        if(serviceDto != null){
            return new ResponseEntity<>(serviceDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<ServiceDto>> getAllServicesBySchoolId(@RequestParam(name = "schoolId") Long id){
        List<ServiceDto> serviceDtoList = serviceService.getAllServicesBySchoolId(id);
        if(!serviceDtoList.isEmpty()){
            return new ResponseEntity<>(serviceDtoList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteServiceById(@PathVariable Long id){
        serviceService.deleteById(id);
    }

}
