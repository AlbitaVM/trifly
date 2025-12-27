package es.albavm.tfg.trifly.dto.Budget;

import java.util.List;

public class CategoryExpendituresDto {
    private Long budgetId;
    private String budgetName;
    private String categoryName;
    private String currencySymbol;
    private List<SummaryExpenditureDto> expenditures;

    public CategoryExpendituresDto(Long budgetId, String budgetName, String categoryName, 
                                   String currencySymbol, List<SummaryExpenditureDto> expenditures) {
        this.budgetId = budgetId;
        this.budgetName = budgetName;
        this.categoryName = categoryName;
        this.currencySymbol = currencySymbol;
        this.expenditures = expenditures;
    }

    // Getters y Setters
    public Long getBudgetId() { return budgetId; }
    public void setBudgetId(Long budgetId) { this.budgetId = budgetId; }
    public String getBudgetName() { return budgetName; }
    public void setBudgetName(String budgetName) { this.budgetName = budgetName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getCurrencySymbol() { return currencySymbol; }
    public void setCurrencySymbol(String currencySymbol) { this.currencySymbol = currencySymbol; }
    public List<SummaryExpenditureDto> getExpenditures() { return expenditures; }
    public void setExpenditures(List<SummaryExpenditureDto> expenditures) { this.expenditures = expenditures; }
}

