package es.albavm.tfg.trifly.Model;

import java.time.LocalDate;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Expenditure {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String concept;
    private double amount;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ExpenditureCategory category;

    public Long getId() {
        return id;
    }

    public String getConcept() {
        return concept;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCategory(ExpenditureCategory category) {
        this.category = category;
    }
}
