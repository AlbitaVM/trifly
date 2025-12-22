package es.albavm.tfg.trifly.Model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import java.sql.Blob;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @Lob
	@Basic(fetch = FetchType.LAZY)
	private Blob imageFile;
	private boolean imageBoolean;

    @ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;

    // Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Itinerary> itineraries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Budget> budgets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reminder> reminders;

    public User() {}

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public List<Itinerary> getItineraries() {
        return itineraries;
    }

    public List<Budget> getBudgets() {
        return budgets;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public void setImageBoolean(boolean imageBoolean) {
        this.imageBoolean = imageBoolean;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets;
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }
}