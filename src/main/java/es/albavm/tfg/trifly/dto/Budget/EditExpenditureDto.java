package es.albavm.tfg.trifly.dto.Budget;

import java.time.LocalDate;

public class EditExpenditureDto {
    private Long id;
    private String concept;
    private double amount;
    private LocalDate date;
    private Long categoryId;
    private Long budgetId;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getBudgetId() { return budgetId; }
    public void setBudgetId(Long budgetId) { this.budgetId = budgetId; }
}
