package es.albavm.tfg.trifly.dto.Budget;

import java.time.LocalDate;

public class CreateExpenditureDto {
    private String concept;
    private double amount;
    private LocalDate date;
    private Long budgetId;
    private Long categoryId;

    public String getConcept() {
        return concept;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
