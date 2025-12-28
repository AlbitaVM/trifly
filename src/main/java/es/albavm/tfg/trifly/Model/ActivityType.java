package es.albavm.tfg.trifly.Model;

public enum ActivityType {
    CULTURAL("Cultural", "museum"),
    GASTRONOMIA("Gastronomía", "restaurant"),
    AVENTURA("Aventura", "hiking"),
    NATURALEZA("Naturaleza", "park"),
    VIDA_NOCTURNA("Vida Nocturna", "nightlife"),
    TRANSPORTE("Transporte", "directions_car"),
    ALOJAMIENTO("Alojamiento", "hotel"),
    OTRO("Otro", "category");

    private final String displayName;
    private final String icon;

    ActivityType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
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
