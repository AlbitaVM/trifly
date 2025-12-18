package es.albavm.tfg.trifly.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.albavm.tfg.trifly.Service.BudgetService;
import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.dto.Budget.CreateBudgetDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;




@Controller
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ItineraryService itineraryService;
    
    @GetMapping("/budget/new")
    public String showNewBudget(Principal principal, Model model) {
        String email = principal.getName();
        model.addAttribute("itineraries", itineraryService.getItinerariesByUser(email));
        return "create-budget"; 
    }

    @PostMapping("/budget/new")
    public String createBudget(@ModelAttribute CreateBudgetDto createBudget, Principal principal) {
        String email = principal.getName();
        budgetService.createBudget(createBudget, email);
        return "redirect:/budgets";
    }


}
