package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.UserDetailsDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String getHomePage(Authentication authentication){
        if(authentication != null){
            System.out.println(authentication.getAuthorities());
            UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
            if(authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals("ROLE_PET_OWNER"))){
                System.out.println("CONTAINS");
            }
            return "search";
        }
        return "home";
    }
}
