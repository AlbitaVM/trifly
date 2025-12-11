package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalTime;

public class ActivityDto {
    private Long id;
    private String activityName;
    private String location;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String activityDescription;
    private String activityType;

    public Long getId() {
        return id;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
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

    public String getActivityType() {
        return activityType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
