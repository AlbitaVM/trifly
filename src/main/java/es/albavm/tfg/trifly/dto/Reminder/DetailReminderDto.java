package es.albavm.tfg.trifly.dto.Reminder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DetailReminderDto {
    private Long id;
    private String reminderTitle;
    private String reminderDescription;
    private LocalDateTime dateTime;


    public DetailReminderDto() {}

    public DetailReminderDto(Long id, String reminderTitle, String reminderDescription, LocalDateTime dateTime) {
        this.id = id;
        this.reminderTitle = reminderTitle;
        this.reminderDescription = reminderDescription;
        this.dateTime = dateTime;
    }

    // Métodos de utilidad para el template
    public boolean hasTitle() {
        return reminderTitle != null && !reminderTitle.trim().isEmpty();
    }

    public boolean hasDescription() {
        return reminderDescription != null && !reminderDescription.trim().isEmpty();
    }

    public boolean hasDateTime() {
        return dateTime != null;
    }

    public String getFormattedDateTime() {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dateTime.format(formatter);
    }

    public String getFormattedDate() {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateTime.format(formatter);
    }

    public String getFormattedTime() {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(formatter);
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(String reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public String getReminderDescription() {
        return reminderDescription;
    }

    public void setReminderDescription(String reminderDescription) {
        this.reminderDescription = reminderDescription;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
