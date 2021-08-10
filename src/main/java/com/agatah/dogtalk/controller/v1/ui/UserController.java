package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.PhotoDto;
import com.agatah.dogtalk.dto.UserDto;
import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.service.PhotoService;
import com.agatah.dogtalk.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
@SessionAttributes("user")
public class UserController {

    private final UserService userService;
    private final PhotoService photoService;

    public UserController(UserService userService, PhotoService photoService){
        this.userService = userService;
        this.photoService = photoService;
    }

    @GetMapping("/info")
    public String showUserInfoTab(Model model, Authentication authentication) {

        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        UserDto user = userService.findUserById(userDetails.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("photo", new PhotoDto());

        return "dashboard/user-info-tab";
    }

    @PutMapping("/info")
    public String updateUserInfo(@ModelAttribute("user") @Validated(UserDto.UserInfo.class) UserDto user,
                             Errors errors, Authentication authentication,
                                 @RequestParam(name = "cancel", required = false) String cancel, Model model){

        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();

        if(cancel != null && cancel.equals("true")){
            UserDto freshUser = new UserDto()
                    .setFirstName(userDetails.getFirstName())
                    .setLastName(userDetails.getLastName())
                    .setEmail(userDetails.getEmail())
                    .setId(userDetails.getUserId())
                    .setPhotoId(user.getPhotoId());

            model.addAttribute("user", freshUser);
        } else {
            if (errors.hasErrors()) {
                return "dashboard/user-info-tab";
            }
            userService.updateUser(userDetails.getUserId(), user);
        }
        return "dashboard/user-info-tab";
    }

    @GetMapping("/password")
    public String showPasswordTab(Model model, Authentication authentication){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        UserDto user = userService.findUserById(userDetails.getUserId());
        model.addAttribute("user", user);
        return "dashboard/change-password-tab";
    }

    @PutMapping("/password")
    public String updatePassword(@ModelAttribute("user") @Validated(UserDto.PasswordInfo.class) UserDto user,
                                 Errors errors, Authentication authentication){

        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        if (errors.hasErrors()) {
            return "dashboard/change-password-tab";
        }
        userService.updatePassword(userDetails.getUserId(), user);
        return "dashboard/change-password-tab";
    }

    @PostMapping("/info/photo")
    public String uploadUserPhoto(@RequestParam("file")MultipartFile file, @ModelAttribute("user") UserDto user) throws IOException {
        Photo photo = new Photo().setImage(file.getBytes());
        userService.addPhoto(user.getId(), photo);
        return "redirect:/user/info";
    }
}
