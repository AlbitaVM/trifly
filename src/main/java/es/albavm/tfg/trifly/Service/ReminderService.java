package es.albavm.tfg.trifly.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.Reminder;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.ReminderRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Reminder.CreateReminderDto;

@Service
public class ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    public void createReminder(CreateReminderDto dto, String email){
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Reminder reminder = new Reminder();
        reminder.setReminderTitle(dto.getReminderTitle());
        reminder.setReminderDescripcion(dto.getReminderDescripcion());
        reminder.setDateTime(dto.getDateTime());
        reminder.setUser(user);

        if(dto.getItineraryId() != null){
            Itinerary itinerary = itineraryRepository.findById(dto.getItineraryId())
                    .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));
            reminder.setItinerary(itinerary);
        }

        reminderRepository.save(reminder);
    }
}
