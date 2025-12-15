package es.albavm.tfg.trifly.dto.Reminder;

import java.time.LocalDateTime;

public class SummaryReminderDto {
    private Long id;
    private String reminderTitle;
    private String reminderDescription;
    private LocalDateTime dateTime;
    private String itineraryName;

    public SummaryReminderDto(Long id, String reminderTitle, String reminderDescription, LocalDateTime dateTime, String itineraryName) {
        this.id = id;
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription != null ? reminderDescription : "Sin descripción";
        this.dateTime = dateTime;
        this.itineraryName = itineraryName;
    }

    public Long getId() {
        return id;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }
}
