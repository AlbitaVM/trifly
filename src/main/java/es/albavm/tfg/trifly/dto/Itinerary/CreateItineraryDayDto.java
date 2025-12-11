package es.albavm.tfg.trifly.dto.Itinerary;

import java.util.ArrayList;
import java.util.List;

public class CreateItineraryDayDto {
    private int numberDay;
    private List<CreateActivityDto> activities = new ArrayList<>();

    public int getNumberDay() {
        return numberDay;
    }

    public List<CreateActivityDto> getActivities() {
        return activities;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }
    
    public void setActivities(List<CreateActivityDto> activities) {
        this.activities = activities;
    }
}
