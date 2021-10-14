package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.BehavioristFullProfileDto;
import com.agatah.dogtalk.dto.ContactDto;
import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.service.BehavioristService;
import com.agatah.dogtalk.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes(names = {"behaviorist", "user"})
public class BehavioristProfileController {

    private final BehavioristService behavioristService;
    private final ContactService contactService;

    @Autowired
    public BehavioristProfileController(BehavioristService behavioristService, ContactService contactService){
        this.behavioristService = behavioristService;
        this.contactService = contactService;
    }

    @GetMapping("/behaviorist/{id}")
    public String  showBehavioristProfile(@PathVariable Long id, Model model){
        model.addAttribute("behaviorist", behavioristService.getBehavioristById(id));
        return "behaviorist-profile";
    }

    @GetMapping("/user/behaviorist")
    public String showBehavioristTab(Model model, Authentication authentication){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        model.addAttribute("behaviorist", behavioristService.getBehavioristById(userDetails.getUserId()));
        return "dashboard/behaviorist-tab";
    }

    @PutMapping("/user/behaviorist/about")
    public String updateBehavioristAbout(@ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist, Model model){
        model.addAttribute("behaviorist", behavioristService.updateBehavioristAbout(behaviorist.getId(), behaviorist));
        return "redirect:/user/behaviorist";
    }

    @PutMapping("/user/behaviorist/qualifications")
    public String updateBehavioristQualifications(@ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist, Model model){
        model.addAttribute("behaviorist", behavioristService.updateBehavioristQualifications(behaviorist.getId(), behaviorist));
        return "redirect:/user/behaviorist";
    }

    @PutMapping("/user/behaviorist/contact")
    public String updateBehavioristContact(@ModelAttribute("contact") ContactDto contact){
        contactService.updateContact(contact);
        return "redirect:/user/behaviorist";
    }

    @DeleteMapping("/user/behaviorist/contact")
    public String deleteBehavioristContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist){
        behavioristService.deleteBehavioristContact(behaviorist.getId(), contact);
        return "redirect:/user/behaviorist";
    }

    @PostMapping("/user/behaviorist/contact")
    public String createBehavioristContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("behaviorist") BehavioristFullProfileDto behaviorist){
        behavioristService.addBehavioristContact(behaviorist.getId(), contact);
        return "redirect:/user/behaviorist";
    }
}
