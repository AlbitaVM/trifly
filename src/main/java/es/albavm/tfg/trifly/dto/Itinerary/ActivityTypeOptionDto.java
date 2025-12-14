package es.albavm.tfg.trifly.dto.Itinerary;

public class ActivityTypeOptionDto {
     private String value;      
    private String label;       
    private boolean selected;   

    public ActivityTypeOptionDto() {}
    
    public ActivityTypeOptionDto(String value, String label, boolean selected) {
        this.value = value;
        this.label = label;
        this.selected = selected;
    }

    // Getters y setters
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isSelected() { return selected; }
    public void setSelected(boolean selected) { this.selected = selected; }
}
