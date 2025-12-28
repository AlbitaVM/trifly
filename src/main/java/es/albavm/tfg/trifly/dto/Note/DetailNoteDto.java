package es.albavm.tfg.trifly.dto.Note;

public class DetailNoteDto {
    private Long id;
    private String noteTitle;
    private String noteDescription;

    public DetailNoteDto() {}

    public DetailNoteDto(Long id, String noteTitle, String noteDescription) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
    }

     public boolean hasTitle() {
        return noteTitle != null && !noteTitle.trim().isEmpty();
    }

    public boolean hasDescription() {
        return noteDescription != null && !noteDescription.trim().isEmpty();
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNoteTitle() { return noteTitle; }
    public void setNoteTitle(String noteTitle) { this.noteTitle = noteTitle; }
    
    public String getNoteDescription() { return noteDescription; }
    public void setNoteDescription(String noteDescription) { 
        this.noteDescription = noteDescription; 
    }
}
