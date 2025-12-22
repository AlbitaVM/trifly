package es.albavm.tfg.trifly.Model;

import java.util.Currency;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private BudgetCurrency currency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "itinerary_id", nullable = true)
    private Itinerary itinerary;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL,  orphanRemoval = true)
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

    public BudgetCurrency getCurrency() {
        return currency;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public User getUser() {
        return user;
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

    public void setCurrency(BudgetCurrency currency) {
        this.currency = currency;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
