package es.albavm.tfg.trifly.Model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Blob;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
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
    private Date startDate;
    private Date finishDate;
    private String state;

    @Lob
	@JsonIgnore
	private Blob imageFile;
	private boolean imageBoolean;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private List<ItineraryDay> days;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private List<Note> notes;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL)
    private List<Reminder> reminders;

    public Itinerary(){

    }

    public Itinerary(String itineraryName,String destination,Date startDate,Date finishDate,String state){
        this.itineraryName = itineraryName;
        this.destination = destination;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.state = state;
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public String getState() {
        return state;
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public void setState(String state) {
        this.state = state;
    }
}
