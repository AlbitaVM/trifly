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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.Service.NoteService;
import es.albavm.tfg.trifly.dto.Note.CreateNoteDto;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

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

        Page<SummaryNoteDto> notes = noteService.getAllNotesPaginated(email, PageRequest.of(page, 3));

        List<Map<String, Object>> pageNumbers = new ArrayList<>();
        for (int i = 0; i < notes.getTotalPages(); i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i);
            pageInfo.put("display", i + 1);
            pageInfo.put("active", i == page);
            pageNumbers.add(pageInfo);
        }
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("isNotes", true);
        model.addAttribute("notes", notes.getContent());
        return "notes";
    }

    @PostMapping("/note/{id}/delete")
    public String deleteNote(@PathVariable Long id, Model model) {
        try {
            noteService.deleteNote(id);
            return "redirect:/notes";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "La nota no coincide con el id");
            return "redirect:/notes";
        }
    }

    @GetMapping("/note/{id}/edit")
    public String showEditNote(Principal principal, @PathVariable Long id, Model model) {
        String email = principal.getName();
        EditNoteDto note = noteService.getNoteForEdit(id, email);
        List<Map<String, Object>> itineraries = itineraryService
                .getItinerariesByUser(email)
                .stream()
                .map(it -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", it.getId());
                    m.put("itineraryName", it.getItineraryName());
                    m.put("selected",
                            it.getId().equals(note.getItineraryId()));
                    return m;
                }).toList();

        model.addAttribute("itineraries", itineraries);
        model.addAttribute("note", note);
        return "/edit-note";
    }

    @PostMapping("/note/{id}/edit")
    public String updateNote(@ModelAttribute EditNoteDto dto, Principal principal) {
        String email = principal.getName();
        noteService.updateNote(email, dto);
        return "redirect:/notes";
    }
    

}
