package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.model.Location;
import com.agatah.dogtalk.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/search")
public class SearchController {

    private LocationService locationService;

    @Autowired
    public SearchController(LocationService locationService){
        this.locationService = locationService;
    }

    @GetMapping
    public String showSearch(Model model){
        model.addAttribute("location", new Location());
        return "search";
    }

    @PostMapping
    public String searchByCity(@ModelAttribute("city") String city){
        return "redirect:/school?city=" + URLEncoder.encode(city, StandardCharsets.UTF_8);
    }
}
