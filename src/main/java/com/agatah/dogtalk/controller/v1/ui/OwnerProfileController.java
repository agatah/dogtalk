package com.agatah.dogtalk.controller.v1.ui;

import com.agatah.dogtalk.dto.OwnerProfileDto;
import com.agatah.dogtalk.dto.PetProfileDto;
import com.agatah.dogtalk.dto.UserDetailsDto;
import com.agatah.dogtalk.model.Photo;
import com.agatah.dogtalk.service.OwnerService;
import com.agatah.dogtalk.service.PetService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
@SessionAttributes("owner")
public class OwnerProfileController {

    private final OwnerService ownerService;
    private final PetService petService;

    public OwnerProfileController(OwnerService ownerService, PetService petService){
        this.ownerService = ownerService;
        this.petService = petService;
    }

    @GetMapping("/owner")
    public String showPetOwnerTab(Model model, Authentication authentication){
        UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
        model.addAttribute("owner", ownerService.getOwnerById(userDetails.getUserId()));
        model.addAttribute("petForm", new PetProfileDto());
        return "dashboard/pet-owner-tab";
    }

    @PostMapping("/owner/pet")
    public String createPetProfile(@ModelAttribute("owner")OwnerProfileDto owner,
                                   @ModelAttribute("petForm") PetProfileDto petProfileDto){
        petService.createPetProfile(owner.getOwnerId(), petProfileDto);
        return "redirect:/user/owner";
    }

    @DeleteMapping("/owner/pet")
    public String deletePetProfile(@ModelAttribute("pet") PetProfileDto pet){
        petService.deletePetProfile(pet.getPetId());
        return "redirect:/user/owner";
    }

    @PutMapping("/owner/pet")
    public String updatePetProfile(@ModelAttribute("pet") PetProfileDto pet){
        petService.updatePetProfile(pet);
        return "redirect:/user/owner";
    }

    @PostMapping("/owner/pet/photo")
    public String uploadPetPhoto(@RequestParam("file") MultipartFile file, @ModelAttribute("pet") PetProfileDto pet)
            throws IOException {

        Photo photo = new Photo().setImage(file.getBytes());
        petService.addPhoto(pet.getPetId(), photo);
        return "redirect:/user/owner";
    }
}
