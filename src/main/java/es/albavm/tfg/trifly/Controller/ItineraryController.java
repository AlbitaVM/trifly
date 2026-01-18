package es.albavm.tfg.trifly.Controller;

import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import es.albavm.tfg.trifly.Model.ActivityType;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.dto.Itinerary.CreateItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.DetailItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.SummaryItineraryDto;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    @ModelAttribute("activityTypes")
    public ActivityType[] showAllActivityTypes() {
        return ActivityType.values();
    }

    @GetMapping("/")
    public String showItineraries(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<SummaryItineraryDto> itineraries = itineraryService.getAllItinerariesPaged(pageable);
        List<Map<String, Object>> pageNumbers = new ArrayList<>();
        for (int i = 0; i < itineraries.getTotalPages(); i++) {
            Map<String, Object> pageInfo = new HashMap<>();
            pageInfo.put("number", i);
            pageInfo.put("display", i + 1);
            pageInfo.put("active", i == page);
            pageNumbers.add(pageInfo);
        }
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("isItineraries", true);
        model.addAttribute("itineraries", itineraries.getContent());

        return "index";
    }

    @GetMapping("/itineraries/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws Exception {
        Itinerary itinerary = itineraryService.findById(id).orElseThrow();

        byte[] imageBytes;
        MediaType mediaType;

        if (itinerary.getImageFile() != null) {
            imageBytes = itinerary.getImageFile().getBytes(1, (int) itinerary.getImageFile().length());
            mediaType = MediaType.IMAGE_PNG;
        } else {
            Resource resource = new ClassPathResource("static/img/itineraries/no_photo.png");
            try (InputStream is = resource.getInputStream()) {
                imageBytes = is.readAllBytes();
            }
            mediaType = MediaType.IMAGE_PNG;
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }

    @GetMapping("/itinerary/new")
    public String showNewItinerary() {
        return "/create-itinerary";
    }

    @PostMapping("/itinerary/new")
    public String createItinerary(@ModelAttribute CreateItineraryDto itineraryDto,
            @RequestParam("imageFile") MultipartFile imageFile, Principal principal) {

        String email = principal.getName();

        itineraryService.createItinerary(itineraryDto, imageFile, email);

        return "redirect:/";
    }

    @PostMapping("/itinerary/{id}/delete")
    public String deleteItinerary(@PathVariable Long id, Model model) {
        try {
            itineraryService.deleteItinerary(id);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "El itinerario no coincide con el id");
            return "redirect:/";
        }
    }

    @GetMapping("/itinerary/{id}/edit")
    public String showEditItinerary(@PathVariable Long id, Model model) {
        try {
            EditItineraryDto editItinerary = itineraryService.getEditItineraryDto(id);
            model.addAttribute("itinerary", editItinerary);
            return "edit-itinerary";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "El itinerario no existe");
            return "redirect:/";
        }
    }

    @PostMapping("/itinerary/{id}/edit")
    public String editActivity(
            @PathVariable Long id,
            @ModelAttribute EditItineraryDto dto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Principal principal) {

        String email = principal.getName();
        dto.setId(id); // Asegurar que el ID está en el DTO
        itineraryService.editItinerary(email, dto, imageFile);
        return "redirect:/profile";
    }

    @GetMapping("/itinerary/{id}/detail")
    public String showItinerarydetails(@PathVariable Long id, Model model) throws JsonProcessingException {
        try {
            DetailItineraryDto itinerary = itineraryService.getDetailItineraryDto(id);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonItinerary = mapper.writeValueAsString(itinerary);

            model.addAttribute("itineraryJson", jsonItinerary);

            model.addAttribute("itinerary", itinerary);

            return "itinerary-detail";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "El itinerario no existe");
            return "redirect:/";
        }
    }

}