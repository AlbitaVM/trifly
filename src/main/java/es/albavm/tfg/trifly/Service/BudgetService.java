package es.albavm.tfg.trifly.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.albavm.tfg.trifly.Model.Budget;
import es.albavm.tfg.trifly.Model.ExpenditureCategory;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.BudgetRepository;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Budget.CreateBudgetDto;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    public void createBudget(CreateBudgetDto dto, String email){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Budget budget = new Budget();
        budget.setBudgetName(dto.getBudgetName());
        budget.setTotal(dto.getTotal());
        budget.setCurrency(dto.getCurrency());
        budget.setUser(user);

        if(dto.getItineraryId() != null){
            Itinerary itinerary = itineraryRepository.findById(dto.getItineraryId())
                .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));
            budget.setItinerary(itinerary);
        }

        List<ExpenditureCategory> categories = dto.getCategories().stream().map(name -> {
            ExpenditureCategory category = new ExpenditureCategory();
            category.setCategoryName(name);
            category.setBudget(budget);
            return category;
        }).toList();

        budget.setCategories(categories);

        budgetRepository.save(budget);
    }

    
}
