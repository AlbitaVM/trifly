package es.albavm.tfg.trifly.dto.Reminder;

import java.time.LocalDateTime;

public class CreateReminderDto {
    private String reminderTitle;
    private String reminderDescripcion;
    private LocalDateTime dateTime;
    private Long itineraryId;

    public String getReminderTitle() {
        return reminderTitle;
    }

    public String getReminderDescripcion() {
        return reminderDescripcion;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public void setReminderDescripcion(String reminderDescripcion) {
        this.reminderDescripcion = reminderDescripcion;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }
}
