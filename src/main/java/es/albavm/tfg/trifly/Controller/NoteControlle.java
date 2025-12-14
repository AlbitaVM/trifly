package es.albavm.tfg.trifly.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.Service.NoteService;
import es.albavm.tfg.trifly.dto.Note.CreateNoteDto;

@Controller
public class NoteControlle {
    @Autowired
    private NoteService noteService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/note/new")
    public String showNewNote(Model model, Principal principal) {
        String email = principal.getName();
        model.addAttribute("itineraries", itineraryService.getItinerariesByUser(email));
        return "create-note";  
    }

    @PostMapping("/note/new")
    public String createNote(@ModelAttribute CreateNoteDto createdNote, Principal principal){
        String email = principal.getName();
        noteService.createNote(createdNote, email);
        return "redirect:/notes";
    }
}
