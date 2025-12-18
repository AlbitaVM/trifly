package es.albavm.tfg.trifly.dto.Budget;

import java.util.Currency;
import java.util.List;

import es.albavm.tfg.trifly.Model.BudgetCurrency;

public class CreateBudgetDto {
    private String budgetName;
    private double total;
    private BudgetCurrency currency;
    private Long itineraryId;
    private List<String> categories;

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

    public List<String> getCategories() {
        return categories;
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

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
