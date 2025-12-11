package es.albavm.tfg.trifly.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.albavm.tfg.trifly.Model.Activity;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Service
public class DatabaseInitializerService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    ItineraryRepository itineraryRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() throws IOException, SQLException {
        // Crear usuario de prueba si no existe
        if (!userRepository.existsByEmail("alba@gmail.com")) {
            User user = new User();
            user.setEmail("alba@gmail.com");
            user.setName("Alba");
            user.setPassword(passwordEncoder.encode("1234")); // Mejor encriptar la contraseña
            userRepository.save(user);
        }

        // Crear itinerarios de prueba si no existen
        createItineraryIfNotExists("Viaje a Paris", "Paris, Francia", 
                LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 12), 
                "static/img/itineraries/itinerary1.jpg");

        createItineraryIfNotExists("Viaje a Madrid", "Madrid, España", 
                LocalDate.of(2025, 7, 8), LocalDate.of(2025, 8, 9), 
                "static/img/itineraries/itinerary2.jpg");

        createItineraryIfNotExists("Viaje a Portugal", "Portugal", 
                LocalDate.of(2025, 2, 11), LocalDate.of(2025, 5, 12), 
                "static/img/itineraries/itinerary3.jpg");

        createItineraryIfNotExists("Viaje a Colombia", "Bogota, Colombia", 
                LocalDate.of(2025, 1, 2), LocalDate.of(2025, 3, 2), 
                "static/img/itineraries/itinerary4.jpg");

        createItineraryIfNotExists("Viaje a Suecia", "Suecia", 
                LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 12), 
                "static/img/itineraries/itinerary5.jpg");

        createItineraryIfNotExists("Viaje a Holanda", "Amsterdam, Holanda", 
                LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 12), 
                "static/img/itineraries/itinerary6.jpg");

        createItineraryIfNotExists("Viaje a Egipto", "Egipto", 
                LocalDate.of(2025, 5, 10), LocalDate.of(2025, 5, 12), 
                "static/img/itineraries/itinerary7.jpg");
    }

    private void createItineraryIfNotExists(String name, String destination, LocalDate start, LocalDate finish, String imagePath) throws IOException, SQLException {
        // Comprobar si el itinerario ya existe por nombre
        if (itineraryRepository.existsByItineraryName(name)) {
            return; // Ya existe, no hacer nada
        }

        Itinerary itinerary = new Itinerary(name, destination);
        itinerary.setStartDate(start);
        itinerary.setFinishDate(finish);

        setItineraryImage(itinerary, imagePath);

        itineraryRepository.save(itinerary);
    }

    private void setItineraryImage(Itinerary itinerary, String classpathResource) throws IOException, SQLException {
        itinerary.setImageBoolean(true);

        // Cargar recurso desde classpath
        Resource resource = new ClassPathResource(classpathResource);

        if (!resource.exists()) {
            throw new FileNotFoundException("No se encontró el recurso: " + classpathResource);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            byte[] data = inputStream.readAllBytes();
            itinerary.setImageFile(new SerialBlob(data));
        }
    }
}
