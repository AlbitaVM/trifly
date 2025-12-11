package es.albavm.tfg.trifly.Service;

import java.util.List;
import java.util.Optional;
import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.albavm.tfg.trifly.Model.Activity;
import es.albavm.tfg.trifly.Model.ActivityType;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.ItineraryDay;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Itinerary.CreateItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.ItineraryDayDto;
import es.albavm.tfg.trifly.dto.Itinerary.SummaryItineraryDto;

@Service
public class ItineraryService {
    
    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<SummaryItineraryDto> getAllItinerariesPaged(Pageable pageable){
        return itineraryRepository.findAll(pageable).map(it -> new SummaryItineraryDto(
            it.getId(), 
            it.getItineraryName(),
            it.getDestination(),
            it.getStartDate(),
            it.getFinishDate(),
            it.getImageBoolean()
            )
        );
    }

    public Optional<Itinerary> findById(Long id){
        return itineraryRepository.findById(id);
    }

    public void createItinerary(CreateItineraryDto dto , MultipartFile imageFile, String email){
         User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        Itinerary itinerary = new Itinerary();
        itinerary.setItineraryName(dto.getItineraryName());
        itinerary.setDestination(dto.getDestination());
        itinerary.setStartDate(dto.getStartDate());
        itinerary.setFinishDate(dto.getFinishDate());
        itinerary.setState("Borrador");
        itinerary.setImageBoolean(false);
        itinerary.setUser(user);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Blob blob = new SerialBlob(imageFile.getBytes());
                itinerary.setImageFile(blob);
                itinerary.setImageBoolean(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        List<ItineraryDay> days = dto.getDays().stream().map(dayDto -> {
            ItineraryDay day = new ItineraryDay();
            day.setNumberDay(dayDto.getNumberDay());
            day.setItinerary(itinerary);

            List<Activity> activities = dayDto.getActivities().stream().map(activityDto -> {
                Activity activity = new Activity();
                activity.setActivityName(activityDto.getActivityName());
                activity.setLocation(activityDto.getLocation());
                activity.setStartTime(activityDto.getStartTime());
                activity.setFinishTime(activityDto.getFinishTime());
                activity.setActivityDescription(activityDto.getActivityDescription());
                activity.setActivityType(activityDto.getActivityType());
                activity.setDay(day);
                return activity;
            }).toList();

            day.setActivity(activities);
            return day;
        }).toList();

        itinerary.setDays(days);
        itineraryRepository.save(itinerary);
    }

}
