package es.albavm.tfg.trifly.dto.Budget;

import java.util.List;

import es.albavm.tfg.trifly.Model.BudgetCurrency;

public class BudgetDetailDto {
    private Long id;
    private String budgetName;
    private double total;
    private double totalSpent;
    private double remaining;
    private boolean overBudget;
    private double exceededAmount;
    private double exceededPercentage;
    private BudgetCurrency currency; 
    private String itineraryName;
    private List<CategorySummaryDto> categories;

    public BudgetDetailDto(Long id, String budgetName, double total, double totalSpent, double remaining, BudgetCurrency currency, boolean overBudget, double exceededAmount, double exceededPercentage, String itineraryName, List<CategorySummaryDto> categories) {
        this.id = id;
        this.budgetName = budgetName;
        this.total = total;
        this.totalSpent = totalSpent;
        this.remaining = remaining;
        this.overBudget = overBudget;
        this.exceededAmount = exceededAmount;
        this.exceededPercentage = exceededPercentage;
        this.currency = currency;
        this.itineraryName = itineraryName;
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

    public String getItineraryName() {
        return itineraryName;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public double getRemaining() {
        return remaining;
    }

    public boolean isOverBudget() {
        return overBudget;
    }

    public BudgetCurrency getCurrency() {
        return currency;
    }

    public String getCurrencySymbol() {
        return currency != null ? currency.getSymbol() : "";
    }

    public List<CategorySummaryDto> getCategories() {
        return categories;
    }

    public double getExceededAmount() {
        return exceededAmount;
    }

    public double getExceededPercentage() {
        return exceededPercentage;
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

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public void setCurrency(BudgetCurrency currency) {
        this.currency = currency;
    }

    public void setCategories(List<CategorySummaryDto> categories) {
        this.categories = categories;
    }

    public void setExceededAmount(double exceededAmount) {
        this.exceededAmount = exceededAmount;
    }

    public void setExceededPercentage(double exceededPercentage) {
        this.exceededPercentage = exceededPercentage;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public void setOverBudget(boolean overBudget) {
        this.overBudget = overBudget;
    }
}
