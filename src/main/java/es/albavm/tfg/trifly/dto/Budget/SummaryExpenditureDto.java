package es.albavm.tfg.trifly.dto.Budget;

import java.time.LocalDate;

public class SummaryExpenditureDto {
    private Long id;
    private String concept;
    private double amount;
    private LocalDate date;
    private String categoryName;


    public SummaryExpenditureDto(Long id, String concept, double amount, LocalDate date, String categoryName) {
        this.id = id;
        this.concept = concept;
        this.amount = amount;
        this.date = date;
        this.categoryName = categoryName;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}