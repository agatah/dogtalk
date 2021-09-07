package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.model.enums.RoleType;
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
            UserDetailsDto userDetailsDto = (UserDetailsDto) authentication.getPrincipal();
            if(authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals(RoleType.ROLE_PET_OWNER.toString()))){

            }
            return "search";
        }
        return "home";
    }
}
