package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(){
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("registerForm", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String processRegisterForm(@ModelAttribute("registerForm") @Validated(UserDto.UserRegister.class) UserDto user,
                                      Errors errors, Model model){

        if(errors.hasErrors()){
            user.setPassword("");
            model.addAttribute("registerForm", user);
            if(userService.isEmailAlreadyRegistered(user)){
                model.addAttribute("emailDuplicated", "this email is already registered");
            }
            return "register";
        }
        userService.createNewUser(user);
        return "redirect:/login";
    }
}
