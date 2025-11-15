package es.albavm.tfg.trifly.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Service.ItineraryService;
import es.albavm.tfg.trifly.dto.Itinerary.SummaryItineraryDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ItineraryController {
    
    @Autowired
    private ItineraryService itineraryService;

   
    @GetMapping("/")
    public String showItineraries(Model model,@RequestParam (defaultValue = "0") int page) {
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
        
        model.addAttribute("itineraries", itineraries.getContent());
         

        return "index";
    }

    @GetMapping("/itineraries/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws Exception {
        Itinerary itinerary = itineraryService.findById(id).orElseThrow();

        if (itinerary.getImageFile() != null) {
            byte[] imageBytes = itinerary.getImageFile().getBytes(1, (int) itinerary.getImageFile().length());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        return ResponseEntity.notFound().build();
    }

    
}
