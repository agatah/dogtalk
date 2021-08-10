package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.*;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@SessionAttributes(value = {"privileges", "school"})
public class SchoolController {

    private SchoolService schoolService;
    private BehavioristService behavioristService;
    private PrivilegeService privilegeService;
    private ContactService contactService;
    private ServiceService serviceService;

    @Autowired
    public SchoolController(SchoolService schoolService, BehavioristService behavioristService,
                            PrivilegeService privilegeService, ContactService contactService,
                            ServiceService serviceService){

        this.schoolService = schoolService;
        this.behavioristService = behavioristService;
        this.privilegeService = privilegeService;
        this.contactService = contactService;
        this.serviceService = serviceService;
    }

    @GetMapping("/school/{id}")
    public String  showSchool(@PathVariable Long id, Model model){
        model.addAttribute("school", schoolService.getSchoolFullById(id));
        return "school";
    }

    @GetMapping(value = "/school", params = "city")
    public String  getAllSchoolShortsByCity(@RequestParam(name = "city") String city, Model model){
        model.addAttribute("schools", schoolService.getAllSchoolShortsByCity(city));
        return "schools_list";
    }

    @GetMapping("/school")
    public String  getAllSchoolShorts(Model model){
        model.addAttribute("schools", schoolService.getAllSchoolShorts());
        return "schools_list";
    }

    @GetMapping("/user/school")
    public String showSchoolsManagementTab(Authentication authentication, Model model){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        BehavioristFullProfileDto behaviorist = behavioristService.getBehavioristByUserId(userDetails.getUserId());
        List<PrivilegeDto> privileges = privilegeService.findAllByBehavioristId(behaviorist.getId());
        model.addAttribute("privileges", privileges);
        model.addAttribute("schoolForm", new SchoolForm());
        return "dashboard/manage-schools-tab";
    }

    @PostMapping("/user/school")
    public String createSchool(@Valid SchoolForm schoolForm, Errors errors,
                               Authentication authentication){

        if(errors.hasErrors()){
            return "dashboard/manage-schools-tab";
        } else {
            UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
            BehavioristFullProfileDto behaviorist = behavioristService.createBehavioristSchool(userDetails.getUserId(), schoolForm.getFormSchoolName());
            return "redirect:/user/school/" + behaviorist.getSchools().get(behaviorist.getSchools().size()-1).getId();
        }
    }

    @GetMapping("/user/school/{id}")
    public String showSchoolTab(Authentication authentication, Model model, @PathVariable(value = "id", required = false) Long schoolId){
        model.addAttribute("school", schoolService.getSchoolFullById(schoolId));
        return "dashboard/school-tab";
    }

    @PutMapping("/user/school/contact")
    public String updateSchoolContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("school") SchoolFullDto school){
        contactService.updateContact(contact);
        return "redirect:/user/school/" + school.getId();
    }

    @DeleteMapping("/user/school/contact")
    public String deleteSchoolContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("school") SchoolFullDto school){
        schoolService.deleteSchoolContact(school.getId(), contact);
        return "redirect:/user/school/" + school.getId();
    }

    @PostMapping("/user/school/contact")
    public String createSchoolContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("school") SchoolFullDto school, Model model){
        schoolService.addSchoolContact(school.getId(), contact);
        return "redirect:/user/school/" + school.getId();
    }

    @PutMapping("/user/school/service")
    public String updateSchoolService(@ModelAttribute("service") ServiceDto service, @ModelAttribute("school") SchoolFullDto school){
        serviceService.updateService(service);
        return "redirect:/user/school/" + school.getId();
    }

    @DeleteMapping("/user/school/service")
    public String deleteSchoolService(@ModelAttribute("service") ServiceDto service, @ModelAttribute("school") SchoolFullDto school){
        schoolService.deleteSchoolService(school.getId(), service);
        return "redirect:/user/school/" + school.getId();
    }

    @PostMapping("/user/school/service")
    public String createSchoolService(@ModelAttribute("service") ServiceDto service, @ModelAttribute("school") SchoolFullDto school, Model model){
        schoolService.addSchoolService(school.getId(), service);
        return "redirect:/user/school/" + school.getId();
    }

    @PostMapping("/user/school/banner")
    public String uploadSchoolBanner(@RequestParam("file") MultipartFile file, @ModelAttribute("school") SchoolFullDto school) throws IOException {
        Photo photo = new Photo().setImage(file.getBytes());
        schoolService.addPhoto(school.getId(), photo);
        return "redirect:/user/school/" + school.getId();
    }


}
