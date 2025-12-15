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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.Service.ReminderService;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;
import es.albavm.tfg.trifly.dto.Reminder.CreateReminderDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ReminderController {
    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ItineraryService itineraryService;

    @GetMapping("/reminders")
    public String showNotes() {
        return "reminders";
    }

    @GetMapping("/reminder/new")
    public String showNewReminder(Principal principal, Model model) {
        
        String email = principal.getName();
        model.addAttribute("itineraries", itineraryService.getItinerariesByUser(email));
        return "/create-reminder";  
    }

    @PostMapping("/reminder/new")
    public String createReminder(@ModelAttribute CreateReminderDto createReminder, Principal principal) {
        String email = principal.getName();
        reminderService.createReminder(createReminder, email);
        return "redirect:/reminders";
    }
    
}
