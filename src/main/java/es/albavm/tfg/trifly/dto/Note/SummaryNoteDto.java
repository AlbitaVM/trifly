package es.albavm.tfg.trifly.dto.Note;

public class SummaryNoteDto {
    private Long id;
    private String noteTitle;
    private String noteDescription;
    private String itineraryName;

    public SummaryNoteDto(Long id, String noteTitle, String noteDescription, String itineraryName) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.itineraryName = itineraryName;
    }

    public Long getId() {
        return id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }
}
