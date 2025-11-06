package es.albavm.tfg.trifly.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String budgetName;
    private double total;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    private List<ExpenditureCategory> categories;

    public Long getId() {
        return id;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public List<ExpenditureCategory> getCategories() {
        return categories;
    }

    public double getTotal() {
        return total;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public void setCategories(List<ExpenditureCategory> categories) {
        this.categories = categories;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
