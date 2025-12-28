package es.albavm.tfg.trifly.dto.Itinerary;

import java.util.List;

public class DetailDayDto {
    private int numberDay;
    private List<DetailActivityDto> activities;

    public DetailDayDto() {}

    public DetailDayDto(int numberDay, List<DetailActivityDto> activities) {
        this.numberDay = numberDay;
        this.activities = activities;
    }

    public int getNumberDay() {
        return numberDay;
    }

    public List<DetailActivityDto> getActivities() {
        return activities;
    }

    public void setNumberDay(int numberDay) {
        this.numberDay = numberDay;
    }

    public void setActivities(List<DetailActivityDto> activities) {
        this.activities = activities;
    }
}
