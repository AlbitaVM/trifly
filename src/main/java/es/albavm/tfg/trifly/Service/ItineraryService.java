package es.albavm.tfg.trifly.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.sql.Blob;
import java.time.LocalTime;

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
import es.albavm.tfg.trifly.Model.Note;
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
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;

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

    public List<Itinerary> getItinerariesByUser(String userEmail) {
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
        if (days == null)
            days = new ArrayList<>();
        days.forEach(day -> {
            if (day.getActivity() == null)
                day.setActivity(new ArrayList<>());
        });

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
                    activityDto.setStartTime(activity.getStartTime());
                } else {
                    activityDto.setStartTime(LocalTime.MIDNIGHT);
                }
                if (activity.getFinishTime() != null) {
                    activityDto.setFinishTime(activity.getFinishTime());
                } else {
                    activityDto.setFinishTime(LocalTime.MIDNIGHT);
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

    public void editItinerary(String email, EditItineraryDto updatedItinerary, MultipartFile imageFile) {
    Itinerary itinerary = itineraryRepository.findById(updatedItinerary.getId())
        .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));

    if (!itinerary.getUser().getEmail().equals(email)) {
        throw new RuntimeException("Acceso no permitido");
    }

    // Actualizar campos básicos
    itinerary.setItineraryName(updatedItinerary.getItineraryName());
    itinerary.setDestination(updatedItinerary.getDestination());
    itinerary.setStartDate(updatedItinerary.getStartDate());
    itinerary.setFinishDate(updatedItinerary.getFinishDate());

    // Procesar imagen si se subió una nueva
    if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Blob blob = new SerialBlob(imageFile.getBytes());
                itinerary.setImageFile(blob);
                itinerary.setImageBoolean(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    // LIMPIAR días existentes (orphanRemoval se encarga de eliminar de BD)
    itinerary.getDays().clear();

    // CREAR NUEVOS días desde el DTO
    for (EditItineraryDayDto dayDto : updatedItinerary.getDays()) {
        ItineraryDay day = new ItineraryDay();
        day.setNumberDay(dayDto.getNumberDay());
        day.setItinerary(itinerary);

        // CREAR NUEVAS actividades
        List<Activity> activities = new ArrayList<>();
        for (EditActivityDto activityDto : dayDto.getActivities()) {
            Activity activity = new Activity();
            activity.setActivityName(activityDto.getActivityName());
            activity.setActivityDescription(activityDto.getActivityDescription());
            activity.setLocation(activityDto.getLocation());
            activity.setActivityType(activityDto.getActivityType());
            activity.setStartTime(activityDto.getStartTime() != null ? activityDto.getStartTime() : LocalTime.MIDNIGHT);
            activity.setFinishTime(activityDto.getFinishTime() != null ? activityDto.getFinishTime() : LocalTime.MIDNIGHT);
            activity.setDay(day);
            
            activities.add(activity);
        }
        
        day.setActivity(activities);
        itinerary.getDays().add(day);
    }

    // Un solo save gracias a CascadeType.ALL
    itineraryRepository.save(itinerary);
}
}