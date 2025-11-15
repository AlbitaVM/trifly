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
    public void init()throws IOException, URISyntaxException, SQLException{
        User user1 = new User("Alba","alba@gmail.com",passwordEncoder.encode("1234"));
        userRepository.save(user1);

        Itinerary itinerary1 = new Itinerary("Viaje a Paris","Paris,Francia");
        itinerary1.setStartDate(LocalDate.of(2025, 5, 10));
        itinerary1.setFinishDate(LocalDate.of(2025, 5, 12));
        setItineraryImage(itinerary1,"static/img/itineraries/itinerary1.jpg");

        itineraryRepository.save(itinerary1);

        Itinerary itinerary2 = new Itinerary("Viaje a Madrid","Madrid,España");
        itinerary2.setStartDate(LocalDate.of(2025, 7, 8));
        itinerary2.setFinishDate(LocalDate.of(2025, 8, 9));
        setItineraryImage(itinerary2,"/static/img/itineraries/itinerary2.jpg");

        itineraryRepository.save(itinerary2);

        Itinerary itinerary3 = new Itinerary("Viaje a Portugal","Portugal");
        itinerary3.setStartDate(LocalDate.of(2025, 2, 11));
        itinerary3.setFinishDate(LocalDate.of(2025, 5, 12));
        setItineraryImage(itinerary3,"/static/img/itineraries/itinerary3.jpg");

        itineraryRepository.save(itinerary3);

        Itinerary itinerary4 = new Itinerary("Viaje a Colombia","Bogota,Colombia");
        itinerary4.setStartDate(LocalDate.of(2025, 1, 2));
        itinerary4.setFinishDate(LocalDate.of(2025, 3, 2));
        setItineraryImage(itinerary4,"/static/img/itineraries/itinerary4.jpg");

        itineraryRepository.save(itinerary4);

        Itinerary itinerary5 = new Itinerary("Viaje a Suecia","Suecia");
        itinerary5.setStartDate(LocalDate.of(2025, 5, 10));
        itinerary5.setFinishDate(LocalDate.of(2025, 5, 12));
        setItineraryImage(itinerary5,"/static/img/itineraries/itinerary5.jpg");

        itineraryRepository.save(itinerary5);

        Itinerary itinerary6 = new Itinerary("Viaje a Holanda","Amsterdam,Holanda");
        itinerary6.setStartDate(LocalDate.of(2025, 5, 10));
        itinerary6.setFinishDate(LocalDate.of(2025, 5, 12));
        setItineraryImage(itinerary6,"/static/img/itineraries/itinerary6.jpg");

        itineraryRepository.save(itinerary6);

        Itinerary itinerary7 = new Itinerary("Viaje a Egipto","Egipto");
        itinerary7.setStartDate(LocalDate.of(2025, 5, 10));
        itinerary7.setFinishDate(LocalDate.of(2025, 5, 12));
        setItineraryImage(itinerary7,"/static/img/itineraries/itinerary7.jpg");

        itineraryRepository.save(itinerary7);
    }

   public void setItineraryImage(Itinerary itinerary, String classpathResource) throws IOException, SQLException {
    itinerary.setImageBoolean(true);
    
    // Prueba diferentes formas de cargar el recurso
    System.out.println("Intentando cargar: " + classpathResource);
    
    // Opción 1: ClassPathResource
    Resource resource = new ClassPathResource(classpathResource);
    System.out.println("Resource exists: " + resource.exists());
    System.out.println("Resource path: " + resource.getURL());
    
    if (!resource.exists()) {
        // Listar recursos disponibles para debug
        System.out.println("El recurso no existe. Verifica la estructura de carpetas.");
        throw new FileNotFoundException("No se encontró el recurso: " + classpathResource);
    }

    try (InputStream inputStream = resource.getInputStream()) {
        byte[] data = inputStream.readAllBytes();
        SerialBlob blob = new SerialBlob(data);
        itinerary.setImageFile(blob);
    }
}
}
