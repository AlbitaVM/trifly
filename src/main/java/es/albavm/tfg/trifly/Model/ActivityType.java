package es.albavm.tfg.trifly.Model;

public enum ActivityType {
    CULTURAL("Cultural"),
    GASTRONOMIA("Gastronomía"),
    AVENTURA( "Aventura"),
    NATURALEZA("Naturaleza"),
    VIDA_NOCTURNA("Vida Nocturna"),
    TRANSPORTE("Transporte"),
    ALOJAMIENTO("Alojamiento"),
    OTRO("Otro");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ActivityType fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            return OTRO;
        }
        
        for (ActivityType type : ActivityType.values()) {
            if (type.name().equalsIgnoreCase(text.replace("-", "_"))) {
                return type;
            }
        }
        return OTRO;
    }
}
