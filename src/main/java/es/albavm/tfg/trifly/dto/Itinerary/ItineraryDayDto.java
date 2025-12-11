package es.albavm.tfg.trifly.dto.Itinerary;

import java.util.List;

public class ItineraryDayDto {
    private Long id;
    private int numberDay;
    private List<ActivityDto> activities;

    public Long getId() {
        return id;
    }
    public int getNumberDay() {
        return numberDay;
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActivities(List<ActivityDto> activities) {
        this.activities = activities;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }
}
