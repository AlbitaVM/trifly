package es.albavm.tfg.trifly.Service;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.albavm.tfg.trifly.Model.Note;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Repository.BudgetRepository;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.Security.CSRFHandlerConfiguration;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;
import es.albavm.tfg.trifly.dto.User.AdminStatsDto;
import es.albavm.tfg.trifly.dto.User.EditProfileDto;
import es.albavm.tfg.trifly.dto.User.ProfileDto;
import es.albavm.tfg.trifly.dto.User.SummaryUserDto;
import es.albavm.tfg.trifly.dto.User.UserDto;
import es.albavm.tfg.trifly.dto.User.UserMapper;

@Service
public class UserService {

    private final CSRFHandlerConfiguration CSRFHandlerConfiguration;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItineraryRepository itineraryRepository;

    @Autowired
    BudgetRepository budgetRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    UserService(CSRFHandlerConfiguration CSRFHandlerConfiguration) {
        this.CSRFHandlerConfiguration = CSRFHandlerConfiguration;
    }

    private UserDto toDTO(User user) {
        return userMapper.toDto(user);
    }

    private Collection<UserDto> toDTOs(Collection<User> users) {
        return userMapper.toDTOs(users);
    }

    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto getUserDto(Long id) {
        return toDTO(userRepository.findById(id).orElseThrow());
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public ProfileDto getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = user.getRoles()
                .stream()
                .anyMatch(r -> r.equals("ROLE_ADMIN"));

        return new ProfileDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                isAdmin);
    }

    public EditProfileDto getProfileForEdit(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new EditProfileDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public void updateProfile(String email, EditProfileDto updatedProfile, MultipartFile imageFile) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(updatedProfile.getName());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Blob blob = new SerialBlob(imageFile.getBytes());
                user.setImageFile(blob);
                user.setImageBoolean(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (updatedProfile.getNewPassword() != null && !updatedProfile.getNewPassword().isBlank()) {

            if (!passwordEncoder.matches(updatedProfile.getCurrentPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Contraseña actual incorrecta");
            }

            if (!updatedProfile.getNewPassword().equals(updatedProfile.getConfirmPassword())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }

            if (updatedProfile.getNewPassword().length() < 6) {
                throw new IllegalArgumentException("Contraseña demasiado corta");
            }

            user.setPassword(passwordEncoder.encode(updatedProfile.getNewPassword()));
        }
        userRepository.save(user);
    }

    public Page<SummaryUserDto> getAllUsersPaginated(String email, Pageable pageable) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return userRepository.findAll(pageable).map(u -> new SummaryUserDto(
                u.getId(),
                u.getName(),
                u.getEmail()));
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public List<Integer> getUsersPerMonth(int year) {

        List<Object[]> results = userRepository.countUsersByMonth(year);

        List<Integer> usersPerMonth = new ArrayList<>();

        // Inicializamos los 12 meses a 0
        for (int i = 0; i < 12; i++) {
            usersPerMonth.add(0);
        }

        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();   // convierte Long a int
            int count = ((Number) row[1]).intValue();
            usersPerMonth.set(month - 1, count);
        }

        
        return usersPerMonth;
    }

    public List<Integer> getItinerariesPerMonth(int year) {

        List<Object[]> results = itineraryRepository.countItinerariesByMonth(year);

        List<Integer> itnerariesPerMonth = new ArrayList<>();

        // Inicializamos los 12 meses a 0
        for (int i = 0; i < 12; i++) {
            itnerariesPerMonth.add(0);
        }

        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();   // convierte Long a int
            int count = ((Number) row[1]).intValue();
            itnerariesPerMonth.set(month - 1, count);
        }
        return itnerariesPerMonth;
    }

    public double getItinerariesGrowthThisMonth() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int currentMonth = today.getMonthValue();

        List<Integer> itinerariesPerMonth = getItinerariesPerMonth(year);

        int thisMonth = itinerariesPerMonth.get(currentMonth - 1);
        int lastMonth = currentMonth > 1 ? itinerariesPerMonth.get(currentMonth - 2) : 0;

        if (lastMonth == 0) {
            return thisMonth > 0 ? 100.0 : 0.0; // crecimiento 100% si antes no había itinerarios
        }

        double growth = ((double)(thisMonth - lastMonth) / lastMonth) * 100;
        return growth;
    }


    public AdminStatsDto getProfileStats() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int currentMonth = today.getMonthValue();
        AdminStatsDto stats = new AdminStatsDto();

        stats.setTotalUsers(userRepository.count());
        stats.setTotalItineraries(itineraryRepository.count());
        stats.setTotalBudgets(budgetRepository.count());

        stats.setUsersPerMonth(getUsersPerMonth(year));
        stats.setItinerariesPerMonth(getItinerariesPerMonth(year));
        
        List<Integer> usersPerMonth = getUsersPerMonth(today.getYear());
        stats.setNewUsersThisMonth(usersPerMonth.get(currentMonth - 1));

        stats.setAvgItinerariesPerUser( userRepository.avgItinerariesPerUser());

        stats.setGrowthThisMonth(getItinerariesGrowthThisMonth());

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long activeUsers = userRepository.countActiveUsers(thirtyDaysAgo);
        long inactiveUsers = userRepository.countTotalUsers() - activeUsers;

        stats.setActiveUsers(activeUsers);
        stats.setInactiveUsers(inactiveUsers);

        return stats;
    }
}
