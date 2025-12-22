package es.albavm.tfg.trifly.dto.Budget;

public class EditCategoryDto {
    private String name;
    private String label;
    private boolean selected;

    public EditCategoryDto(String name, String label, boolean selected) {
        this.name = name;
        this.label = label;
        this.selected = selected;
    }

    public String getName() { return name; }
    public String getLabel() { return label; }
    public boolean isSelected() { return selected; }
}
