package es.albavm.tfg.trifly.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import es.albavm.tfg.trifly.Model.Budget;
import es.albavm.tfg.trifly.Service.BudgetService;
import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.dto.Budget.BudgetDetailDto;
import es.albavm.tfg.trifly.dto.Budget.CreateBudgetDto;
import es.albavm.tfg.trifly.dto.Budget.SummaryBudgetDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/budgets")
    public String showBudgets(Principal principal, @RequestParam(defaultValue = "0") int page, Model model) {
        String email = principal.getName();

        Page<SummaryBudgetDto> budgets = budgetService.getAllBudgetsPaginated(email, PageRequest.of(page, 3));

        List<Map<String, Object>> pageNumbers = new ArrayList<>();
        for (int i = 0; i < budgets.getTotalPages(); i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i);
            pageInfo.put("display", i + 1);
            pageInfo.put("active", i == page);
            pageNumbers.add(pageInfo);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("budgets", budgets.getContent());
        return "/budgets";  
    }

    @PostMapping("/budget/{id}/delete")
    public String deleteBudget(@PathVariable Long id, Model model) {
       try{
            budgetService.deleteBudget(id);
            return "redirect:/budgets";
       }catch (RuntimeException e){
            model.addAttribute("errorMessage", "El presupuesto no coincide con el id");
            return "redirect:/budgets";
       }
    }

    @GetMapping("/budget/{id}/detail")
    public String showBudgetDetail(@PathVariable Long id, Principal principal, Model model) {
        String email = principal.getName();
        BudgetDetailDto budget = budgetService.getBudgetDetail(id, email);
        model.addAttribute("budgetDetail", budget);
        return "budget-detail"; 
    }
}

