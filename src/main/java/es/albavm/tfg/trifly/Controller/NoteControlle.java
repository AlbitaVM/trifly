package es.albavm.tfg.trifly.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.Service.NoteService;
import es.albavm.tfg.trifly.dto.Note.CreateNoteDto;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;

import org.springframework.web.bind.annotation.RequestParam;

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
    public String createNote(@ModelAttribute CreateNoteDto createdNote, Principal principal) {
        String email = principal.getName();
        noteService.createNote(createdNote, email);
        return "redirect:/notes";
    }

    @GetMapping("/notes")
    public String showNotes(Model model, @RequestParam(defaultValue = "0") int page, Principal principal) {

        String email = principal.getName();

        Page<SummaryNoteDto> notes= noteService.getAllNotesPaginated(email, PageRequest.of(page, 3));

        List<Map<String, Object>> pageNumbers = new ArrayList<>();
        for (int i = 0; i < notes.getTotalPages(); i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i);
            pageInfo.put("display", i + 1);
            pageInfo.put("active", i == page);
            pageNumbers.add(pageInfo);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("notes", notes.getContent());
        return "notes";
    }

}
