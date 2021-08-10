package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.service.BehavioristService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes(names = {"behaviorist", "user"})
public class BehavioristProfileController {

    private final BehavioristService behavioristService;

    @Autowired
    public BehavioristProfileController(BehavioristService behavioristService){
        this.behavioristService = behavioristService;
    }

    @GetMapping("/behaviorist/{id}")
    public String  showBehavioristProfile(@PathVariable Long id, Model model){
        model.addAttribute("behaviorist", behavioristService.getBehavioristById(id));
        return "behaviorist_profile";
    }

    @GetMapping("/user/behaviorist")
    public String showBehavioristTab(Model model, Authentication authentication){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        model.addAttribute("behaviorist", behavioristService.getBehavioristByUserId(userDetails.getUserId()));
        return "dashboard/behaviorist-tab";
    }

    @PutMapping("/user/behaviorist")
    public String updateBehavioristInfo(@ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist, Model model){
        model.addAttribute("behaviorist", behavioristService.updateBehavioristInfo(behaviorist.getId(), behaviorist));
        return "dashboard/behaviorist-tab";
    }

    @PutMapping("/user/behaviorist/contact")
    public String updateBehavioristContact(@ModelAttribute("contact") ContactDto contact){
        behavioristService.updateBehavioristContact(contact);
        return "redirect:/user/behaviorist";
    }

    @DeleteMapping("/user/behaviorist/contact")
    public String deleteBehavioristContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist){
        behavioristService.deleteBehavioristContact(behaviorist.getId(), contact);
        return "redirect:/user/behaviorist";
    }

    @PostMapping("/user/behaviorist/contact")
    public String createBehavioristContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist, Model model){
        behavioristService.addBehavioristContact(behaviorist.getId(), contact);
        return "redirect:/user/behaviorist";
    }


}
