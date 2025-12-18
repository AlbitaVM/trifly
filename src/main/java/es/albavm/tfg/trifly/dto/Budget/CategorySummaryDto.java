package es.albavm.tfg.trifly.dto.Budget;

public class CategorySummaryDto {
    private String categoryName;
    private double spent;
    private double percentage;
    private String categoryClass;

    public CategorySummaryDto(String categoryName, double spent, double percentage, String categoryClass) {
    this.categoryName = categoryName;
    this.spent = spent;
    this.percentage = percentage;
    this.categoryClass = categoryClass;
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
}
