package com.agatah.dogtalk.controller.v1.ui;

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
            if(authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals("ROLE_PET_OWNER"))){
                return "owner-home";
            } else if(authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals("ROLE_BEHAVIORIST"))){
                return "behaviorist-home";
            }
        }
        return "home";
    }
}
