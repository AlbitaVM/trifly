package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CreateItineraryDto {
    private String itineraryName;
    private String destination;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String state;
    private List<CreateItineraryDayDto> days = new ArrayList<>();

    public String getItineraryName() {
        return itineraryName;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public String getState() {
        return state;
    }

    public List<CreateItineraryDayDto> getDays() {
        return days;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDays(List<CreateItineraryDayDto> days) {
        this.days = days;
    }
}
