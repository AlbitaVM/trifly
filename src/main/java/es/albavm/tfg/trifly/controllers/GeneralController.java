package es.albavm.tfg.trifly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
     

     @GetMapping("/itinerary")
    public String showdetails() {
        return "itinerary-detail"; 
    }

    @GetMapping("/notes")
    public String showdnotes() {
        return "notes"; 
    }

    @GetMapping("/budget")
    public String showbudget() {
        return "budget-detail"; 
    }



    @GetMapping("/budget/new")
    public String showBudgetDetail() {
        return "create-budget"; 
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

   

     @GetMapping("/reminder/new")
    public String showNewReminder() {
        return "/create-reminder";  
    }

     @GetMapping("/budgets")
    public String showBudgets() {
        return "/budgets";  
    }

    @GetMapping("/budget/edit")
    public String showEditBudget() {
        return "/edit-budget";  
    }

    @GetMapping("/note/edit")
    public String showEditNote() {
        return "/edit-note";  
    }

    @GetMapping("/reminder/edit")
    public String showEditReminder() {
        return "/edit-reminder";  
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

