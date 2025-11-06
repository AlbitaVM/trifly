package es.albavm.tfg.trifly.Model;

import java.time.LocalTime;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String activityName;
    private String location;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String activityDescription;

    @ManyToOne
    @JoinColumn(name = "day_id")
    private ItineraryDay day;

    public Long getId() {
        return id;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getLocation() {
        return location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
}
