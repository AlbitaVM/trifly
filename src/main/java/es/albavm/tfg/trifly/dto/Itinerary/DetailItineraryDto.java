package es.albavm.tfg.trifly.dto.Itinerary;

import java.time.LocalDate;
import java.util.List;

import es.albavm.tfg.trifly.dto.Note.DetailNoteDto;
import es.albavm.tfg.trifly.dto.Reminder.DetailReminderDto;

public class DetailItineraryDto {
    private Long id;
    private String itineraryName;
    private String destination;
    private LocalDate startDate;
    private LocalDate finishDate;
    private int totalDays;
    private boolean imageBoolean;
    private List<DetailDayDto> days;
    private List<DetailNoteDto> notes;
    private List<DetailReminderDto> reminders;
    private boolean hasBudget; 
    private Long budgetId;

    public DetailItineraryDto() {}

    public int getTotalDays() {
        return days != null ? days.size() : 0;
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

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<DetailNoteDto> getNotes() {
        return notes;
    }

    public List<DetailReminderDto> getReminders() {
        return reminders;
    }

    public List<DetailDayDto> getDays() {
        return days;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public boolean hasBudget() {
        return hasBudget;
    }

    public boolean isHasBudget() {
        return hasBudget;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
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

    public void setDays(List<DetailDayDto> days) {
        this.days = days;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public void setHasBudget(boolean hasBudget) {
        this.hasBudget = hasBudget;
    }

    public void setNotes(List<DetailNoteDto> notes) {
        this.notes = notes;
    }

    public void setReminders(List<DetailReminderDto> reminders) {
        this.reminders = reminders;
    }

    public void setImageBoolean(boolean imageBoolean) {
        this.imageBoolean = imageBoolean;
    }
}
