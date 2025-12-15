package es.albavm.tfg.trifly.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.Note;
import es.albavm.tfg.trifly.Model.Reminder;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.ReminderRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Reminder.CreateReminderDto;
import es.albavm.tfg.trifly.dto.Reminder.EditReminderDto;
import es.albavm.tfg.trifly.dto.Reminder.SummaryReminderDto;

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
        reminder.setReminderDescription(dto.getReminderDescription());
        reminder.setDateTime(dto.getDateTime());
        reminder.setUser(user);

        if(dto.getItineraryId() != null){
            Itinerary itinerary = itineraryRepository.findById(dto.getItineraryId())
                    .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));
            reminder.setItinerary(itinerary);
        }

        reminderRepository.save(reminder);
    }

    public Page<SummaryReminderDto> getAllRemindersPaginated(String email, Pageable pageable){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return reminderRepository.findByUser(user, pageable).map(reminder -> new SummaryReminderDto(
            reminder.getId(),
            reminder.getReminderTitle(),
            reminder.getReminderDescription(),
            reminder.getDateTime(),
            reminder.getItinerary() != null ? reminder.getItinerary().getItineraryName() : null
        ));
    }

     public void deleteReminder(Long id){
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if(optionalReminder.isPresent()){
            reminderRepository.delete(optionalReminder.get());
        }else {
            throw new RuntimeException("Reminder not found");
        }
    }

    public EditReminderDto getReminderForEdit(Long id, String email){
        userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recordatorio no encontrado"));

        return new EditReminderDto(
        reminder.getId(),
        reminder.getReminderTitle(),
        reminder.getReminderDescription(),
        reminder.getDateTime(),
        reminder.getItinerary() != null ? reminder.getItinerary().getId() : null);
    }

    public void updateReminder(String email, EditReminderDto updatedReminder){
        Reminder reminder = reminderRepository.findById(updatedReminder.getId())
            .orElseThrow(() -> new RuntimeException("Recordatorio no encontrada"));

        if (!reminder.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Acceso no permitido");
        }

        reminder.setReminderTitle(updatedReminder.getReminderTitle());
        reminder.setReminderDescription(updatedReminder.getReminderDescription());
        reminder.setDateTime(updatedReminder.getDateTime());
        if (updatedReminder.getItineraryId() != null) {
            Itinerary it = itineraryRepository
                    .findById(updatedReminder.getItineraryId())
                    .orElseThrow();
            reminder.setItinerary(it);
        } else {
            reminder.setItinerary(null);
        }
         reminderRepository.save(reminder);
    }
}
