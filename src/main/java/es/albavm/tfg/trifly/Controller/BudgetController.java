package es.albavm.tfg.trifly.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import es.albavm.tfg.trifly.Model.Budget;
import es.albavm.tfg.trifly.Model.BudgetCurrency;
import es.albavm.tfg.trifly.Service.BudgetService;
import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.dto.Budget.BudgetDetailDto;
import es.albavm.tfg.trifly.dto.Budget.CategoryExpendituresDto;
import es.albavm.tfg.trifly.dto.Budget.CreateBudgetDto;
import es.albavm.tfg.trifly.dto.Budget.CreateExpenditureDto;
import es.albavm.tfg.trifly.dto.Budget.EditBudgetDto;
import es.albavm.tfg.trifly.dto.Budget.EditCategoryDto;
import es.albavm.tfg.trifly.dto.Budget.EditExpenditureDto;
import es.albavm.tfg.trifly.dto.Budget.SummaryBudgetDto;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
        model.addAttribute("isBudgets", true);
        model.addAttribute("budgets", budgets.getContent());
        return "/budgets";
    }

    @PostMapping("/budget/{id}/delete")
    public String deleteBudget(@PathVariable Long id, Model model) {
        try {
            budgetService.deleteBudget(id);
            return "redirect:/budgets";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "El presupuesto no coincide con el id");
            return "redirect:/budgets";
        }
    }

    @GetMapping("/budget/{id}/detail")
    public String showBudgetDetail(@PathVariable Long id, Principal principal, Model model, HttpServletRequest request) {
        String email = principal.getName();
        BudgetDetailDto budget = budgetService.getBudgetDetail(id, email);
        
        // Pasar el token CSRF al modelo
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        
        model.addAttribute("budgetDetail", budget);
        model.addAttribute("_csrf", csrfToken);
        
        return "budget-detail";
    }

    @GetMapping("/bill/{budgetId}/new")
    public String showNewBill(@PathVariable Long budgetId, Principal principal, Model model) {
        String email = principal.getName();

        Budget budget = budgetService.getBudgetForUser(budgetId, email);

        model.addAttribute("categories", budget.getCategories());
        model.addAttribute("budgetId", budgetId);

        return "/create-bill";
    }

    @PostMapping("/bill/{budgetId}/new")
    public String createExpense(@PathVariable Long budgetId, @ModelAttribute CreateExpenditureDto dto,
            Principal principal) {
        String email = principal.getName();
        budgetService.createBill(dto, email);
        return "redirect:/budget/" + budgetId + "/detail";
    }

    @GetMapping("/budget/{id}/edit")
    public String editBudgetForm(@PathVariable Long id, Principal principal, Model model) {
        String email = principal.getName();

        EditBudgetDto budget = budgetService.getBudgetForEdit(id, email);

        List<Map<String, Object>> itineraries = itineraryService
                .getItinerariesByUser(email)
                .stream()
                .map(it -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", it.getId());
                    m.put("itineraryName", it.getItineraryName());
                    m.put("selected",
                            it.getId().equals(budget.getItineraryId()));
                    return m;
                }).toList();

        model.addAttribute("itineraries", itineraries);

        model.addAttribute("budget", budget); 
        model.addAttribute("categories", budget.getCategories());
        model.addAttribute("isEUR", budget.getCurrency() == BudgetCurrency.EUR);
        model.addAttribute("isGBP", budget.getCurrency() == BudgetCurrency.GBP);
        model.addAttribute("isJPY", budget.getCurrency() == BudgetCurrency.JPY);

        return "edit-budget";
    }

    @PostMapping("/budget/{id}/edit")
    public String updateBudget(@ModelAttribute EditBudgetDto dto, Principal principal, Model model) {
        String email = principal.getName();
        try {
            List<EditCategoryDto> categories = dto.getSelectedCategories().stream()
                .map(name -> new EditCategoryDto(name, capitalize(name), true))
                .toList();

            dto.setCategories(categories);

            budgetService.updateBudget(email, dto);
            return "redirect:/budgets";
        } catch (RuntimeException e) {
            model.addAttribute("error", "No puedes desmarcar categorías que tienen gastos asociados");
            EditBudgetDto budget = budgetService.getBudgetForEdit(dto.getId(), email);
            model.addAttribute("budget", budget);

            // Reconstruir todas las categorías para mostrar checkboxes correctos
            model.addAttribute("categories", budgetService.buildCategoriesForEdit(
                    budgetService.getBudget(dto.getId()) 
            ));

            // Flags de moneda
            model.addAttribute("isEUR", budget.getCurrency() == BudgetCurrency.EUR);
            model.addAttribute("isGBP", budget.getCurrency() == BudgetCurrency.GBP);
            model.addAttribute("isJPY", budget.getCurrency() == BudgetCurrency.JPY);
            
            return "edit-budget";
        }
    }

    private String capitalize(String value) {
        return value.charAt(0) + value.substring(1).toLowerCase();
    }

    @GetMapping("/budget/{budgetId}/category/{categoryName}/expenditures")
    @ResponseBody
    public CategoryExpendituresDto getCategoryExpenditures(
            @PathVariable Long budgetId,
            @PathVariable String categoryName,
            Principal principal) {
        String email = principal.getName();
        return budgetService.getCategoryExpenditures(budgetId, categoryName, email);
    }

    @GetMapping("/expenditure/{id}/edit")
    public String editExpenditureForm(@PathVariable Long id, Principal principal, Model model) {
        String email = principal.getName();
        EditExpenditureDto expenditure = budgetService.getExpenditureForEdit(id, email);
        
        Budget budget = budgetService.getBudget(expenditure.getBudgetId());
        model.addAttribute("expenditure", expenditure);
        model.addAttribute("categories", budget.getCategories());
        
        return "edit-expenditure";
    }

    @PostMapping("/expenditure/{id}/edit")
    public String updateExpenditure(@PathVariable Long id, @ModelAttribute EditExpenditureDto dto, Principal principal) {
        String email = principal.getName();
        budgetService.updateExpenditure(id, dto, email);
        return "redirect:/budget/" + dto.getBudgetId() + "/detail";
    }


}
