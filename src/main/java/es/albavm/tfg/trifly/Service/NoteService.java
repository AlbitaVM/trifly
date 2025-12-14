package es.albavm.tfg.trifly.Service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.Note;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.NoteRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Note.CreateNoteDto;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    public void createNote(CreateNoteDto dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Note note = new Note();
        note.setNoteTitle(dto.getNoteTitle());
        note.setNoteDescription(dto.getNoteDescription());
        note.setUser(user);

        if (dto.getItineraryId() != null) {
            Itinerary itinerary = itineraryRepository.findById(dto.getItineraryId())
                    .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));
            note.setItinerary(itinerary);
        }

        noteRepository.save(note);
    }

    public Page<SummaryNoteDto> getAllNotesPaginated(String email, Pageable pageable) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return noteRepository.findByUser(user, pageable).map(note -> new SummaryNoteDto(
                note.getId(),
                note.getNoteTitle(),
                note.getNoteDescription(),
                note.getItinerary() != null ? note.getItinerary().getItineraryName() : null
            ));
    }
}
