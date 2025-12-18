package es.albavm.tfg.trifly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
     

     @GetMapping("/itinerary")
    public String showdetails() {
        return "itinerary-detail"; 
    }



    @GetMapping("/budget")
    public String showbudget() {
        return "budget-detail"; 
    }

    @GetMapping("/itinerary/detail/map")
    public String showMapDetail() {
        return "/journal-map-detail";  
    }

    @GetMapping("/foreignExchange")
    public String showForeignExchange() {
        return "/foreign-exchange";  
    }

   

    @GetMapping("/itinerary/new/ai")
    public String showNewIAItinerary() {
        return "/create-itinerary-ai";  
    }

    

     @GetMapping("/bill/new")
    public String showNewBill() {
        return "/create-bill";  
    }

   

   

    

    @GetMapping("/budget/edit")
    public String showEditBudget() {
        return "/edit-budget";  
    }

    

   

    @GetMapping("/profile")
    public String showProfile() {
        return "/profile";  
    }

    @GetMapping("/profile/edit")
    public String showFormEdit() {
        return "/edit-profile";  
    }
}

