package es.albavm.tfg.trifly.dto.Budget;

public class CategorySummaryDto {
    private Long id;
    private String categoryName;
    private double spent;
    private double percentage;
    private String categoryClass;
    private String currencySymbol;

    public CategorySummaryDto(Long id, String categoryName, double spent, double percentage, String categoryClass, String currencySymbol) {
    this.id = id;
    this.categoryName = categoryName;
    this.spent = spent;
    this.percentage = percentage;
    this.categoryClass = categoryClass;
    this.currencySymbol = currencySymbol;
    }

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getSpent() {
        return spent;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getCategoryClass() {
        return categoryClass;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setCategoryClass(String categoryClass) {
        this.categoryClass = categoryClass;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
