package es.albavm.tfg.trifly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
     

   

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

    

    

   

   


   

    
}

