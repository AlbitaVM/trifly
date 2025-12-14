package es.albavm.tfg.trifly.dto.Note;

public class CreateNoteDto {
    private String noteTitle;
    private String noteDescription;
    private Long itineraryId;

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }
}
