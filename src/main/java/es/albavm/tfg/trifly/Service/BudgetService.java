package es.albavm.tfg.trifly.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import es.albavm.tfg.trifly.Model.Budget;
import es.albavm.tfg.trifly.Model.Expenditure;
import es.albavm.tfg.trifly.Model.ExpenditureCategory;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.Note;
import es.albavm.tfg.trifly.Model.Reminder;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.BudgetRepository;
import es.albavm.tfg.trifly.Repository.ExpenditureCategoryRepository;
import es.albavm.tfg.trifly.Repository.ExpenditureRepository;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Budget.BudgetDetailDto;
import es.albavm.tfg.trifly.dto.Budget.CategorySummaryDto;
import es.albavm.tfg.trifly.dto.Budget.CreateBudgetDto;
import es.albavm.tfg.trifly.dto.Budget.CreateExpenditureDto;
import es.albavm.tfg.trifly.dto.Budget.EditBudgetDto;
import es.albavm.tfg.trifly.dto.Budget.EditCategoryDto;
import es.albavm.tfg.trifly.dto.Budget.SummaryBudgetDto;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import es.albavm.tfg.trifly.dto.Reminder.SummaryReminderDto;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenditureRepository expenditureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private ExpenditureCategoryRepository categoryRepository;

    public void createBudget(CreateBudgetDto dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Budget budget = new Budget();
        budget.setBudgetName(dto.getBudgetName());
        budget.setTotal(dto.getTotal());
        budget.setCurrency(dto.getCurrency());
        budget.setUser(user);

        if (dto.getItineraryId() != null) {
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

    public Page<SummaryBudgetDto> getAllBudgetsPaginated(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return budgetRepository.findByUser(user, pageable).map(budget -> new SummaryBudgetDto(
                budget.getId(),
                budget.getBudgetName(),
                budget.getTotal(),
                budget.getCurrency(),
                budget.getItinerary() != null ? budget.getItinerary().getItineraryName() : null));
    }

    public void deleteBudget(Long id) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);
        if (optionalBudget.isPresent()) {
            budgetRepository.delete(optionalBudget.get());
        } else {
            throw new RuntimeException("Reminder not found");
        }
    }

    public double calculateTotalSpent(Long budgetId) {
        return expenditureRepository.sumAmountByBudgetId(budgetId);
    }

    public double calculateRemaining(Budget budget) {
        return budget.getTotal() - calculateTotalSpent(budget.getId());
    }

    private List<CategorySummaryDto> buildCategorySummaries(Budget budget) {
        List<String> colors = List.of("red", "cyan", "yellow", "pink", "blue");
        AtomicInteger index = new AtomicInteger(0);
        return budget.getCategories().stream()
                .map(cat -> {
                    double totalSpent = cat.getExpenditure().stream().mapToDouble(Expenditure::getAmount).sum();
                    double percentage = budget.getTotal() > 0 ? (totalSpent / budget.getTotal()) * 100 : 0;
                    String colorClass = colors.get(index.getAndIncrement() % colors.size());
                    return new CategorySummaryDto(
                            cat.getCategoryName(),
                            totalSpent,
                            Math.round(percentage),
                            colorClass // <-- aquí asignas categoryClass
                    );
                }).toList();
    }

    public BudgetDetailDto getBudgetDetail(Long budgetId, String email) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Acceso no permitido");
        }

        double totalSpent = calculateTotalSpent(budget.getId());
        double remaining = calculateRemaining(budget);

        boolean overBudget = totalSpent > budget.getTotal();
        double exceededAmount = overBudget ? totalSpent - budget.getTotal() : 0;
        double exceededPercentage = overBudget
                ? ((totalSpent - budget.getTotal()) / budget.getTotal()) * 100
                : 0;

        List<CategorySummaryDto> categories = buildCategorySummaries(budget);

        return new BudgetDetailDto(
                budget.getId(),
                budget.getBudgetName(),
                budget.getTotal(),
                totalSpent,
                remaining,
                budget.getCurrency(),
                overBudget,
                exceededAmount,
                Math.round(exceededPercentage),
                budget.getItinerary() != null ? budget.getItinerary().getItineraryName() : null,
                categories);

    }

    public void createBill(CreateExpenditureDto dto, String email) {

        Budget budget = budgetRepository.findById(dto.getBudgetId())
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Acceso no permitido");
        }

        ExpenditureCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Expenditure expense = new Expenditure();
        expense.setConcept(dto.getConcept());
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setCategory(category);

        expenditureRepository.save(expense);
    }

    public Budget getBudgetForUser(Long budgetId, String email) {
        return budgetRepository.findByIdAndUserEmail(budgetId, email)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado o sin permisos"));
    }

    public List<EditCategoryDto> buildCategoriesForEdit(Budget budget) {

        List<String> allCategories = List.of(
                "ALOJAMIENTO",
                "COMIDA",
                "TRANSPORTE",
                "ACTIVIDADES",
                "OTROS");

        Set<String> selectedCategories = budget.getCategories()
                .stream()
                .map(ExpenditureCategory::getCategoryName)
                .collect(Collectors.toSet());

        return allCategories.stream()
                .map(cat -> new EditCategoryDto(
                        cat,
                        capitalize(cat),
                        selectedCategories.contains(cat)))
                .toList();
    }

    private String capitalize(String value) {
        return value.charAt(0) + value.substring(1).toLowerCase();
    }

    public EditBudgetDto getBudgetForEdit(Long id, String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        List<EditCategoryDto> categories = buildCategoriesForEdit(budget);

        return new EditBudgetDto(
                budget.getId(),
                budget.getBudgetName(),
                budget.getTotal(),
                budget.getCurrency(),
                budget.getItinerary() != null ? budget.getItinerary().getId() : null,
                categories);
    }

    public void updateBudget(String email, EditBudgetDto updatedBudget) {
        Budget budget = budgetRepository.findById(updatedBudget.getId())
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrada"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Acceso no permitido");
        }

        budget.setBudgetName(updatedBudget.getBudgetName());
        budget.setTotal(updatedBudget.getTotal());
        budget.setCurrency(updatedBudget.getCurrency());

         Set<String> selectedCategories = updatedBudget.getCategories().stream()
            .filter(EditCategoryDto::isSelected)
            .map(EditCategoryDto::getName)
            .collect(Collectors.toSet());

    // Eliminar categorías desmarcadas si no tienen gastos
    Iterator<ExpenditureCategory> it = budget.getCategories().iterator();
    while (it.hasNext()) {
        ExpenditureCategory cat = it.next();
        if (!selectedCategories.contains(cat.getCategoryName())) {
            if (!cat.getExpenditure().isEmpty()) {
                throw new RuntimeException(
                        "No puedes desmarcar la categoría '" + cat.getCategoryName() +
                        "' porque tiene gastos asociados");
            }
            it.remove();
        }
    }

    // Agregar nuevas categorías seleccionadas
    for (String catName : selectedCategories) {
        boolean exists = budget.getCategories().stream()
                .anyMatch(c -> c.getCategoryName().equals(catName));
        if (!exists) {
            ExpenditureCategory newCat = new ExpenditureCategory();
            newCat.setCategoryName(catName);
            newCat.setBudget(budget);
            budget.getCategories().add(newCat);
        }
    }

        if (updatedBudget.getItineraryId() != null) {
            Itinerary itinerary = itineraryRepository
                    .findById(updatedBudget.getItineraryId())
                    .orElseThrow();
            budget.setItinerary(itinerary);
        } else {
            budget.setItinerary(null);
        }

        budgetRepository.save(budget);
    }

    public Budget getBudget(Long id) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);
        if(optionalBudget.isPresent()){
            return optionalBudget.get();
        }else{
            return null;
        }
    }
}
