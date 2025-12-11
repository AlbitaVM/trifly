package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalTime;

import es.albavm.tfg.trifly.Model.ActivityType;

public class CreateActivityDto {
    private String activityName;
    private String location;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String activityDescription;
    private ActivityType activityType;
    
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

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
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

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
