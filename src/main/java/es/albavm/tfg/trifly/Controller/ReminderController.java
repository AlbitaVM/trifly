package es.albavm.tfg.trifly.Controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.PrinterName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.Service.ReminderService;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;
import es.albavm.tfg.trifly.dto.Reminder.CreateReminderDto;
import es.albavm.tfg.trifly.dto.Reminder.EditReminderDto;
import es.albavm.tfg.trifly.dto.Reminder.SummaryReminderDto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class ReminderController {
    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ItineraryService itineraryService;

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

    @GetMapping("/reminders")
    public String showReminder(Model model, @RequestParam(defaultValue = "0") int page, Principal principal) {

        String email = principal.getName();

        Page<SummaryReminderDto> reminders= reminderService.getAllRemindersPaginated(email, PageRequest.of(page, 3));

        List<Map<String, Object>> pageNumbers = new ArrayList<>();
        for (int i = 0; i < reminders.getTotalPages(); i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i);
            pageInfo.put("display", i + 1);
            pageInfo.put("active", i == page);
            pageNumbers.add(pageInfo);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("reminders", reminders.getContent());
        return "reminders";
    }
    
    @PostMapping("/reminder/{id}/delete")
    public String deleteReminder(@PathVariable Long id, Model model) {
       try{
            reminderService.deleteReminder(id);
            return "redirect:/reminders";
       }catch (RuntimeException e){
            model.addAttribute("errorMessage", "El recordatorio no coincide con el id");
            return "redirect:/reminders";
       }
    }

    @GetMapping("/reminder/{id}/edit")
    public String showEditReminder(Principal principal, @PathVariable Long id, Model model){
        String email = principal.getName();
        EditReminderDto reminder = reminderService.getReminderForEdit(id, email);

        List<Map<String, Object>> itineraries = itineraryService
                .getItinerariesByUser(email)
                .stream()
                .map(it -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", it.getId());
                    m.put("itineraryName", it.getItineraryName());
                    m.put("selected",
                            it.getId().equals(reminder.getItineraryId()));
                    return m;
                }).toList();

        model.addAttribute("itineraries", itineraries);
        model.addAttribute("reminder", reminder);
        return "/edit-reminder";  
    }

    @PostMapping("/reminder/{id}/edit")
    public String updateReminder(@ModelAttribute EditReminderDto dto, Principal principal) {
        String email = principal.getName();
        reminderService.updateReminder(email, dto);
        
        return "redirect:/reminders";
    }
    
}
