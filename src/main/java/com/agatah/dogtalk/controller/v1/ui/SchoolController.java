package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.*;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.model.Privilege;
import com.agatah.dogtalk.model.enums.PrivilegeType;
import com.agatah.dogtalk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@SessionAttributes(value = {"privileges", "school"})
public class SchoolController {

    private final SchoolService schoolService;
    private final BehavioristService behavioristService;
    private final PrivilegeService privilegeService;
    private final ContactService contactService;
    private final ServiceOfferService serviceOfferService;

    @Autowired
    public SchoolController(SchoolService schoolService, BehavioristService behavioristService,
                            PrivilegeService privilegeService, ContactService contactService,
                            ServiceOfferService serviceOfferService){

        this.schoolService = schoolService;
        this.behavioristService = behavioristService;
        this.privilegeService = privilegeService;
        this.contactService = contactService;
        this.serviceOfferService = serviceOfferService;
    }

    @GetMapping("/school/{id}")
    public String  showSchool(@PathVariable Long id, Model model, Authentication authentication){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        model.addAttribute("userId", userDetails.getUserId());
        model.addAttribute("school", schoolService.getSchoolFullById(id));
        return "school";
    }

    @GetMapping("/user/school")
    public String showSchoolsManagementTab(Authentication authentication, Model model){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        BehavioristFullProfileDto behaviorist = behavioristService.getBehavioristById(userDetails.getUserId());
        List<SchoolWithPrivilegesDto> privileges = privilegeService.findAllByBehavioristId(behaviorist.getId());
        model.addAttribute("privileges", privileges);
        SchoolForm schoolForm = new SchoolForm();
        schoolForm.getLocations().add(new LocationDto());
        model.addAttribute("schoolForm", schoolForm);
        return "dashboard/manage-schools-tab";
    }

    @PostMapping("/user/school")
    public String createSchool(@Valid SchoolForm schoolForm, Errors errors, Model model, Authentication authentication){
        model.addAttribute("schoolForm", schoolForm);
        if(errors.hasErrors()){
            return "dashboard/manage-schools-tab";
        } else {
            UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
            BehavioristFullProfileDto behaviorist = behavioristService.createBehavioristSchool(userDetails.getUserId(), schoolForm);
            return "redirect:/user/school/" + behaviorist.getSchoolWithPrivilegesList()
                    .get(behaviorist.getSchoolWithPrivilegesList().size() -1).getSchoolId();
        }
    }

    @PreAuthorize("hasPermission(#schoolId, 'School', 'MANAGE')")
    @DeleteMapping("/user/school")
    public String deleteSchool(Long schoolId){
        schoolService.deleteSchoolById(schoolId);
        return "redirect:/user/school";
    }

    @PostMapping("/user/school/leave")
    public String leaveSchool(Long schoolId, Authentication authentication){
        System.out.println("LEAVE SCHOOL");
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        behavioristService.leaveSchool(userDetails.getUserId(), schoolId);
        return "redirect:/user/school";
    }

    @PreAuthorize("hasPermission(#schoolId, 'School', 'MANAGE')")
    @PostMapping("/user/school/remove")
    public String removeBehavioristFromSchool(Long schoolId, Long behavioristId){
        behavioristService.removeBehavioristFromSchool(behavioristId, schoolId);
        return "redirect:/user/school/" + schoolId;
    }

    @PreAuthorize("hasPermission(#schoolId, 'School', 'MANAGE')")
    @PutMapping("/user/school/privilege")
    public String updateBehavioristPrivileges(Long schoolId, Long behavioristId,  SchoolWithPrivilegesDto schoolWithPrivilegesDto){
        behavioristService.updateBehavioristPrivileges(schoolId, behavioristId, schoolWithPrivilegesDto.getPrivileges());
        return "redirect:/user/school/" + schoolId;
    }

    @PreAuthorize("hasPermission(#schoolId, 'School', 'EDIT')")
    @GetMapping("/user/school/{id}")
    public String showSchoolTab(Authentication authentication, Model model, @PathVariable(value = "id", required = false) Long schoolId){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        model.addAttribute("school", schoolService.getSchoolFullById(schoolId));
        model.addAttribute("userId", userDetails.getUserId());
        model.addAttribute("behavioristPrivileges", new SchoolWithPrivilegesDto());
        return "dashboard/school-tab";
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @PutMapping("/user/school/contact")
    public String updateSchoolContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("school") SchoolFullDto school){
        contactService.updateContact(contact);
        return "redirect:/user/school/" + school.getId();
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @DeleteMapping("/user/school/contact")
    public String deleteSchoolContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("school") SchoolFullDto school){
        schoolService.deleteSchoolContact(school.getId(), contact);
        return "redirect:/user/school/" + school.getId();
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @PostMapping("/user/school/contact")
    public String createSchoolContact(@ModelAttribute("contact") ContactDto contact, @ModelAttribute("school") SchoolFullDto school, Model model){
        schoolService.addSchoolContact(school.getId(), contact);
        return "redirect:/user/school/" + school.getId();
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @PutMapping("/user/school/service")
    public String updateSchoolService(@ModelAttribute("service") ServiceDto service, @ModelAttribute("school") SchoolFullDto school){
        serviceOfferService.updateService(service);
        return "redirect:/user/school/" + school.getId();
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @DeleteMapping("/user/school/service")
    public String deleteSchoolService(@ModelAttribute("service") ServiceDto service, @ModelAttribute("school") SchoolFullDto school){
        schoolService.deleteSchoolService(school.getId(), service);
        return "redirect:/user/school/" + school.getId();
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @PostMapping("/user/school/service")
    public String createSchoolService(@ModelAttribute("service") ServiceDto service, @ModelAttribute("school") SchoolFullDto school, Model model){
        schoolService.addSchoolService(school.getId(), service);
        return "redirect:/user/school/" + school.getId();
    }

    @PreAuthorize("hasPermission(#school.getId(), 'School', 'EDIT')")
    @PostMapping("/user/school/banner")
    public String uploadSchoolBanner(@RequestParam("file") MultipartFile file, @ModelAttribute("school") SchoolFullDto school) throws IOException {
        Photo photo = new Photo().setImage(file.getBytes());
        schoolService.addPhoto(school.getId(), photo);
        return "redirect:/user/school/" + school.getId();
    }

    @PostMapping("/school/{id}/join")
    public String joinSchool(@PathVariable("id") Long schoolId, Authentication authentication){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        behavioristService.addBehavioristToSchool(userDetails.getUserId(), schoolId,
                Arrays.asList(new Privilege().setPrivilegeType(PrivilegeType.JOIN_REQUEST)));
        return "redirect:/school/" + schoolId;
    }


}
