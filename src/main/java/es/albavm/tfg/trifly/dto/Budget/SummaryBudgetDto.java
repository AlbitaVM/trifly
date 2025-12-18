package es.albavm.tfg.trifly.dto.Budget;

import java.time.LocalDateTime;
import java.util.List;

import es.albavm.tfg.trifly.Model.BudgetCurrency;

public class SummaryBudgetDto {
    private Long id;
    private String budgetName;
    private double total;
    private BudgetCurrency currency;
    private String itineraryName;
    private List<String> categories;

    public SummaryBudgetDto(Long id, String budgetName, double total, BudgetCurrency currency, String itineraryName) {
        this.id = id;
        this.budgetName = budgetName;
        this.total = total;
        this.currency = currency;
        this.itineraryName = itineraryName;
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

    public String getItineraryName() {
        return itineraryName;
    }

    public List<String> getCategories() {
        return categories;
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

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}

