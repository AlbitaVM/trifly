package es.albavm.tfg.trifly.dto.Reminder;

import java.time.LocalDateTime;

public class EditReminderDto {
    private Long id;
    private String reminderTitle;
    private String reminderDescription;
    private LocalDateTime dateTime;
    private Long itineraryId;

     public EditReminderDto(Long id, String reminderTitle, String reminderDescription, LocalDateTime dateTime, Long itineraryId) {
        this.id = id;
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.dateTime = dateTime;
        this.itineraryId = itineraryId;
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

    public Long getItineraryId() {
        return itineraryId;
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

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }
}
