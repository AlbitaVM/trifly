package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalDate;

public class SummaryItineraryDto {
    private Long id;
    private String itineraryName;
    private String destination;
    private LocalDate startDate;
    private LocalDate finishDate;
    private boolean imageBoolean;

    public SummaryItineraryDto(Long id, String itineraryName, String destination,
    LocalDate startDate, LocalDate finishDate, boolean imageBoolean) {
        this.id = id;
        this.itineraryName = itineraryName;
        this.destination = destination;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.imageBoolean = imageBoolean;
    }

    public Long getId() {
        return id;
    }

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

    public boolean getImageBoolean() {
        return imageBoolean;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setImageBoolean(boolean imageBoolean) {
        this.imageBoolean = imageBoolean;
    }
}
