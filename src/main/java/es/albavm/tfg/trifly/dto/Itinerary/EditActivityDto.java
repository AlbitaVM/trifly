package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalTime;
import java.util.List;

import es.albavm.tfg.trifly.Model.ActivityType;

public class EditActivityDto {
    private Long id;
    private int index;  
    private int dayIndex;
    private String activityName;
    private String location;
    private String startTime;
    private String finishTime;
    private String activityDescription;
    private ActivityType activityType;

    private List<ActivityTypeOptionDto> activityTypeOptions;

    public List<ActivityTypeOptionDto> getActivityTypeOptions() {
        return activityTypeOptions;
    }
    
    public Long getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
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

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setActivityTypeOptions(List<ActivityTypeOptionDto> activityTypeOptions) {
        this.activityTypeOptions = activityTypeOptions;
    }
}
