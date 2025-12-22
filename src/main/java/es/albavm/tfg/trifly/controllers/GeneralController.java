package es.albavm.tfg.trifly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
     

     @GetMapping("/itinerary")
    public String showdetails() {
        return "itinerary-detail"; 
    }

    @GetMapping("/itinerary/detail/map")
    public String showMapDetail() {
        return "/journal-map-detail";  
    }

    @GetMapping("/foreignExchange")
    public String showForeignExchange(Model model) {
        model.addAttribute("isForeignExchange", true);
        return "/foreign-exchange";  
    }

   

    @GetMapping("/itinerary/new/ai")
    public String showNewIAItinerary() {
        return "/create-itinerary-ai";  
    }

    

    

   

   

    

    @GetMapping("/budget/edit")
    public String showEditBudget() {
        return "/edit-budget";  
    }

    

   

    @GetMapping("/profile")
    public String showProfile(Model model) {
        model.addAttribute("isProfile", true);
        return "/profile";  
    }

    @GetMapping("/profile/edit")
    public String showFormEdit() {
        return "/edit-profile";  
    }
}

