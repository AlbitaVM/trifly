package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalTime;

public class DetailActivityDto {
    private String activityName;
    private String location;
    private LocalTime startTime;
    private LocalTime finishTime;
    private String activityDescription;
    private String activityTypeIcon;
    private String activityTypeName;

    private Double latitud;
    private Double longitud;

    public DetailActivityDto() {}

    public String getFormattedStartTime() {
        return startTime != null ? startTime.toString() : "";
    }

    public String getFormattedFinishTime() {
        return finishTime != null ? finishTime.toString() : "";
    }

    public String getTimeRange() {
        if (startTime != null && finishTime != null) {
            return startTime.toString() + " - " + finishTime.toString();
        }
        return startTime != null ? startTime.toString() : "";
    }

    public String getActivityName() {
        return activityName;
    }

    public String getLocation() {
        return location;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public String getActivityTypeIcon() {
        return activityTypeIcon;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public void setActivityTypeIcon(String activityTypeIcon) {
        this.activityTypeIcon = activityTypeIcon;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
