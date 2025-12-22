package es.albavm.tfg.trifly.dto.Budget;

import java.time.LocalDateTime;
import java.util.List;

import es.albavm.tfg.trifly.Model.BudgetCurrency;
import es.albavm.tfg.trifly.Model.ExpenditureCategory;

public class EditBudgetDto {
    private Long id;
    private String budgetName;
    private double total;
    private BudgetCurrency currency;
    private Long itineraryId;
    private List<EditCategoryDto> categories;

    private List<String> selectedCategories;

    public EditBudgetDto(Long id, String budgetName, double total, BudgetCurrency currency, Long itineraryId, List<EditCategoryDto> categories) {
        this.id = id;
        this.budgetName = budgetName;
        this.total = total;
        this.currency = currency;
        this.itineraryId = itineraryId;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public double getTotal() {
        return total;
    }

    public BudgetCurrency getCurrency() {
        return currency;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public List<EditCategoryDto> getCategories() {
        return categories;
    }

    public List<String> getSelectedCategories() {
        return selectedCategories;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setCurrency(BudgetCurrency currency) {
        this.currency = currency;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public void setCategories(List<EditCategoryDto> categories) {
        this.categories = categories;
    }

    public void setSelectedCategories(List<String> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }
}


