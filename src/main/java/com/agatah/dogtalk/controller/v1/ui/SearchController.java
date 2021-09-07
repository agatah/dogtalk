package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.LocationDto;
import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.service.LocationService;
import com.agatah.dogtalk.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Controller
@RequestMapping("/search")
public class SearchController {

    private final SchoolService schoolService;

    @Autowired
    public SearchController(SchoolService schoolService){
        this.schoolService = schoolService;
    }

    @GetMapping
    public String showSearch(){
        return "search";
    }

    @PostMapping
    public String searchSchoolByCity(@ModelAttribute("city") String city){
        if(city.isBlank()){
            return "redirect:/search/school";
        }
        return "redirect:/search/school?city=" + URLEncoder.encode(city, StandardCharsets.UTF_8);
    }

    @GetMapping(value = "/school")
    public String getSchoolsPage(@RequestParam(name = "city") Optional<String> city,
                                 @RequestParam(name = "page") Optional<Integer> page,
                                 Model model){

        if(city.isPresent()){
            model.addAttribute("location", new LocationDto().setCity(city.get()));
            model.addAttribute("schoolPage", schoolService.getAllSchoolShortsByCity(city.get(), page.orElse(0)));
        } else {
            model.addAttribute("schoolPage", schoolService.getAllSchoolShorts(page.orElse(0)));
        }
        return "search";
    }
}
