package es.albavm.tfg.trifly.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.sql.Blob;
import javax.sql.rowset.serial.SerialBlob;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.transaction.Transactional;
import es.albavm.tfg.trifly.Model.Activity;
import es.albavm.tfg.trifly.Model.ActivityType;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.ItineraryDay;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Itinerary.ActivityTypeOptionDto;
import es.albavm.tfg.trifly.dto.Itinerary.CreateItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditActivityDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditItineraryDayDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.ItineraryDayDto;
import es.albavm.tfg.trifly.dto.Itinerary.SummaryItineraryDto;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<SummaryItineraryDto> getAllItinerariesPaged(Pageable pageable) {
        return itineraryRepository.findAll(pageable).map(it -> new SummaryItineraryDto(
                it.getId(),
                it.getItineraryName(),
                it.getDestination(),
                it.getStartDate(),
                it.getFinishDate(),
                it.getImageBoolean()));
    }

    public Optional<Itinerary> findById(Long id) {
        return itineraryRepository.findById(id);
    }

    public List<Itinerary> getItinerariesByUser(String userEmail){
         User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return itineraryRepository.findByUser(user);
    }

    public void createItinerary(CreateItineraryDto dto, MultipartFile imageFile, String email) {
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

    public void deleteItinerary(Long id) {
        Optional<Itinerary> optionalItinerary = itineraryRepository.findById(id);
        if (optionalItinerary.isPresent()) {
            itineraryRepository.delete(optionalItinerary.get());
        } else {
            throw new RuntimeException("Itinerary not found");
        }
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public EditItineraryDto getEditItineraryDto(Long id) {
        // Usar findById normal pero dentro de una transacción
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));

        EditItineraryDto dto = new EditItineraryDto();
        dto.setId(itinerary.getId());
        dto.setItineraryName(itinerary.getItineraryName());
        dto.setDestination(itinerary.getDestination());
        dto.setStartDate(itinerary.getStartDate());
        dto.setFinishDate(itinerary.getFinishDate());

        // Usar AtomicInteger para el índice del día (empezando en 0)
        AtomicInteger dayIndex = new AtomicInteger(0);

        // Forzar la carga de las colecciones lazy
        List<ItineraryDay> days = itinerary.getDays();
        if (days == null || days.isEmpty()) {
            dto.setDays(new ArrayList<>());
            return dto;
        }

        List<EditItineraryDayDto> dayDtos = days.stream().map(day -> {
            EditItineraryDayDto dayDto = new EditItineraryDayDto();
            int currentDayIndex = dayIndex.getAndIncrement();
            dayDto.setNumberDay(day.getNumberDay());
            dayDto.setIndex(currentDayIndex); // NUEVO: establecer el índice (0, 1, 2...)

            // Usar AtomicInteger para el índice de actividad (empezando en 0)
            AtomicInteger activityIndex = new AtomicInteger(0);

            // Forzar la carga de las actividades lazy
            List<Activity> activities = day.getActivity();
            if (activities == null || activities.isEmpty()) {
                dayDto.setActivities(new ArrayList<>());
                return dayDto;
            }

            List<EditActivityDto> activityDtos = activities.stream().map(activity -> {
                EditActivityDto activityDto = new EditActivityDto();
                int currentActivityIndex = activityIndex.getAndIncrement();
                activityDto.setIndex(currentActivityIndex); // NUEVO: índice de actividad (0, 1, 2...)
                activityDto.setDayIndex(currentDayIndex); // NUEVO: índice del día padre (0, 1, 2...)
                activityDto.setActivityName(activity.getActivityName());
                activityDto.setActivityDescription(activity.getActivityDescription());
                activityDto.setActivityType(activity.getActivityType());
                activityDto.setLocation(activity.getLocation());
                if (activity.getStartTime() != null) {
                    activityDto.setStartTime(activity.getStartTime().toString());
                }
                if (activity.getFinishTime() != null) {
                    activityDto.setFinishTime(activity.getFinishTime().toString());
                }

                List<ActivityTypeOptionDto> activityTypeOptions = Arrays.stream(ActivityType.values())
                        .map(type -> new ActivityTypeOptionDto(
                                type.name(),
                                type.getDisplayName(),
                                type == activity.getActivityType() // selected si coincide
                )).toList();
                activityDto.setActivityTypeOptions(activityTypeOptions);

                return activityDto;
            }).toList();

            dayDto.setActivities(activityDtos);
            return dayDto;
        }).toList();

        dto.setDays(dayDtos);
        return dto;
    }

}
