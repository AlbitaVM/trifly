package es.albavm.tfg.trifly.Model;

import jakarta.persistence.Id;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ItineraryDay {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberDay;

    @ManyToOne
    @JoinColumn(name = "itinerary_id")
    private Itinerary itinerary;

    @OneToMany(mappedBy = "day", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Activity> activity;

    public Long getId() {
        return id;
    }

    public int getNumberDay() {
        return numberDay;
    }

    public List<Activity> getActivity() {
        return activity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }
}
