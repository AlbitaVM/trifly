package es.albavm.tfg.trifly.dto.Itinerary;

import java.util.ArrayList;
import java.util.List;

public class EditItineraryDayDto {
    private Long id;
    private int index;
    private int numberDay;
    private List<EditActivityDto> activities = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public int getNumberDay() {
        return numberDay;
    }

    public List<EditActivityDto> getActivities() {
        return activities;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }
    
    public void setActivities(List<EditActivityDto> activities) {
        this.activities = activities;
    }
}


