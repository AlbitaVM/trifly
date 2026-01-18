package es.albavm.tfg.trifly.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import es.albavm.tfg.trifly.Model.Budget;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.ItineraryDay;
import es.albavm.tfg.trifly.Model.Note;
import es.albavm.tfg.trifly.Model.Reminder;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.dto.Itinerary.ActivityTypeOptionDto;
import es.albavm.tfg.trifly.dto.Itinerary.CreateItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.DetailActivityDto;
import es.albavm.tfg.trifly.dto.Itinerary.DetailDayDto;
import es.albavm.tfg.trifly.dto.Itinerary.DetailItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditActivityDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditItineraryDayDto;
import es.albavm.tfg.trifly.dto.Itinerary.EditItineraryDto;
import es.albavm.tfg.trifly.dto.Itinerary.ItineraryDayDto;
import es.albavm.tfg.trifly.dto.Itinerary.SummaryItineraryDto;
import es.albavm.tfg.trifly.dto.Note.DetailNoteDto;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import es.albavm.tfg.trifly.dto.Reminder.DetailReminderDto;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NominatimService nominatimService;

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
                if (activityDto.getLocation() != null && !activityDto.getLocation().trim().isEmpty()) {
                    try {
                        Map<String, Double> coordinates = nominatimService.geocodeAddress(activityDto.getLocation());
                        if (coordinates != null) {
                            activity.setLatitud(coordinates.get("latitude"));
                            activity.setLongitud(coordinates.get("longitude"));
                            System.out.println("✓ Geocodificado: " + activityDto.getLocation());
                        } else {
                            System.out.println("✗ No se pudo geocodificar: " + activityDto.getLocation());
                        }
                    } catch (Exception e) {
                        System.err.println("✗ Error geocodificando: " + activityDto.getLocation() + " - " + e.getMessage());
                    }
                }
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
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));

        EditItineraryDto dto = new EditItineraryDto();
        dto.setId(itinerary.getId());
        dto.setItineraryName(itinerary.getItineraryName());
        dto.setDestination(itinerary.getDestination());
        dto.setStartDate(itinerary.getStartDate());
        dto.setFinishDate(itinerary.getFinishDate());

        AtomicInteger dayIndex = new AtomicInteger(0);

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

        itinerary.setItineraryName(updatedItinerary.getItineraryName());
        itinerary.setDestination(updatedItinerary.getDestination());
        itinerary.setStartDate(updatedItinerary.getStartDate());
        itinerary.setFinishDate(updatedItinerary.getFinishDate());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Blob blob = new SerialBlob(imageFile.getBytes());
                itinerary.setImageFile(blob);
                itinerary.setImageBoolean(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        itinerary.getDays().clear();

        for (EditItineraryDayDto dayDto : updatedItinerary.getDays()) {
            ItineraryDay day = new ItineraryDay();
            day.setNumberDay(dayDto.getNumberDay());
            day.setItinerary(itinerary);

            List<Activity> activities = new ArrayList<>();
            for (EditActivityDto activityDto : dayDto.getActivities()) {
                Activity activity = new Activity();
                activity.setActivityName(activityDto.getActivityName());
                activity.setActivityDescription(activityDto.getActivityDescription());
                activity.setLocation(activityDto.getLocation());
                activity.setActivityType(activityDto.getActivityType());
                activity.setStartTime(
                        activityDto.getStartTime() != null ? activityDto.getStartTime() : LocalTime.MIDNIGHT);
                activity.setFinishTime(
                        activityDto.getFinishTime() != null ? activityDto.getFinishTime() : LocalTime.MIDNIGHT);
                
                if (activityDto.getLocation() != null && !activityDto.getLocation().trim().isEmpty()) {
                    try {
                        Map<String, Double> coordinates = nominatimService.geocodeAddress(activityDto.getLocation());
                        if (coordinates != null) {
                            activity.setLatitud(coordinates.get("latitude"));
                            activity.setLongitud(coordinates.get("longitude"));
                            System.out.println("✓ Geocodificado (edit): " + activityDto.getLocation());
                        }
                    } catch (Exception e) {
                        System.err.println("✗ Error geocodificando (edit): " + activityDto.getLocation());
                    }
                }
                activity.setDay(day);

                activities.add(activity);
            }

            day.setActivity(activities);
            itinerary.getDays().add(day);
        }

        itineraryRepository.save(itinerary);
    }


    public DetailItineraryDto getDetailItineraryDto(Long id){
         Itinerary itinerary = itineraryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));

        DetailItineraryDto itineraryDto = new DetailItineraryDto();
        itineraryDto.setId(itinerary.getId());
        itineraryDto.setItineraryName(itinerary.getItineraryName());
        itineraryDto.setDestination(itinerary.getDestination());
        itineraryDto.setStartDate(itinerary.getStartDate());
        itineraryDto.setFinishDate(itinerary.getFinishDate());

        List<Budget> budgets = itinerary.getBudgets();

        if (budgets != null && !budgets.isEmpty()) {
            itineraryDto.setHasBudget(true);
            itineraryDto.setBudgetId(budgets.get(0).getId()); 
        } else {
            itineraryDto.setHasBudget(false);
        }

        List<DetailDayDto> daysDto = new ArrayList<>();
        List<ItineraryDay> days = itinerary.getDays();

        if(days != null){
            for(ItineraryDay day :days){
                List<DetailActivityDto> activityDtos = new ArrayList<>();
                if (day.getActivity() != null) {
                     for (Activity activity : day.getActivity()) {
                        DetailActivityDto activityDto = new DetailActivityDto();
                        activityDto.setActivityName(activity.getActivityName());
                        activityDto.setLocation(activity.getLocation());
                        activityDto.setStartTime(activity.getStartTime());
                        activityDto.setFinishTime(activity.getFinishTime());
                        activityDto.setActivityDescription(activity.getActivityDescription());
                        
                        if (activity.getActivityType() != null) {
                            activityDto.setActivityTypeIcon(activity.getActivityType().getIcon());
                            activityDto.setActivityTypeName(activity.getActivityType().getDisplayName());
                        }
                        
                        activityDto.setLatitud(activity.getLatitud());
                        activityDto.setLongitud(activity.getLongitud());
                        
                        activityDtos.add(activityDto);
                     }
                }
                 DetailDayDto dayDto = new DetailDayDto(day.getNumberDay(), activityDtos);
                daysDto.add(dayDto);
            }
        }
        itineraryDto.setDays(daysDto);

        List<DetailNoteDto> noteDtos = new ArrayList<>();
        if (itinerary.getNotes() != null && !itinerary.getNotes().isEmpty()) {
            for (Note note : itinerary.getNotes()) {
                DetailNoteDto noteDto = new DetailNoteDto(
                    note.getId(),
                    note.getNoteTitle(),
                    note.getNoteDescription()
                );
                noteDtos.add(noteDto);
            }
        }
        itineraryDto.setNotes(noteDtos);

        List<DetailReminderDto> reminderDtos = new ArrayList<>();
        if (itinerary.getReminders() != null && !itinerary.getReminders().isEmpty()) {
            for (Reminder reminder : itinerary.getReminders()) {
                DetailReminderDto reminderDto = new DetailReminderDto(
                    reminder.getId(),
                    reminder.getReminderTitle(),
                    reminder.getReminderDescription(),
                    reminder.getDateTime()
                );
                reminderDtos.add(reminderDto);
            }
        }
        itineraryDto.setReminders(reminderDtos);

        return itineraryDto;
    }
}