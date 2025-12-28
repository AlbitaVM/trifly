package es.albavm.tfg.trifly.Model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Blob;
import java.time.LocalDate;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Itinerary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itineraryName;
    private String destination;
    private LocalDate startDate;
    private LocalDate finishDate;
    private String state;

    @Lob
	@Basic(fetch = FetchType.LAZY)
	private Blob imageFile;
	private boolean imageBoolean;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryDay> days;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Note> notes;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reminder> reminders;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Budget> budgets;

    public Itinerary(){

    }

    public Itinerary(String itineraryName,String destination){
        this.itineraryName = itineraryName;
        this.destination = destination;
        
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

    public String getState() {
        return state;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public boolean getImageBoolean() {
        return imageBoolean;
    }

    public List<ItineraryDay> getDays() {
        return days;
    }

    public User getUser() {
        return user;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public List<Reminder> getReminders() {
        return reminders;
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

    public void setState(String state) {
        this.state = state;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public void setImageBoolean(boolean imageBoolean) {
        this.imageBoolean = imageBoolean;
    }

    public void setDays(List<ItineraryDay> days) {
        this.days = days;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }
    
}
