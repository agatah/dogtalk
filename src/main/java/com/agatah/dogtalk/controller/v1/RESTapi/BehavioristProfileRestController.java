package com.agatah.dogtalk.controller.v1.RESTapi;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.BehavioristShortProfileDto;
import com.agatah.dogtalk.service.BehavioristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/behaviorists")
public class BehavioristProfileRestController {

    private BehavioristService behavioristService;

    @Autowired
    public BehavioristProfileRestController(BehavioristService behavioristService){
        this.behavioristService = behavioristService;
    }

    @GetMapping
    public List<BehavioristFullProfileDto> getAllBehaviorists(){
        return behavioristService.getAllBehaviorists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BehavioristFullProfileDto> getBehavioristByID(@PathVariable Long id){
        BehavioristFullProfileDto behavioristDto = behavioristService.getBehavioristById(id);
        if(behavioristDto != null){
            return new ResponseEntity<>(behavioristDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/short")
    public List<BehavioristShortProfileDto> getAllBehavioristShorts(){
        return behavioristService.getAllBehavioristsShort();
    }

    @GetMapping("/short/{id}")
    public ResponseEntity<BehavioristShortProfileDto> getBehavioristShortById(@PathVariable Long id){
        BehavioristShortProfileDto behavioristDto = behavioristService.getBehavioristShortById(id);
        if(behavioristDto != null){
            return new ResponseEntity<>(behavioristDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }




}
