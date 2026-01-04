package es.albavm.tfg.trifly.dto.User;

public class ProfileDto {
    private Long id;
    private String name;
    private String email;
    private boolean admin;

    public ProfileDto(Long id, String name, String email, boolean admin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.admin = admin;
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

    public boolean isAdmin() {
        return admin;
    }
}
