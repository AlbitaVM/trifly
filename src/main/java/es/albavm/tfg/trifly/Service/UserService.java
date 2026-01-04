package es.albavm.tfg.trifly.Service;

import java.sql.Blob;
import java.util.Collection;
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
import es.albavm.tfg.trifly.Repository.UserRepository;
import es.albavm.tfg.trifly.Security.CSRFHandlerConfiguration;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import es.albavm.tfg.trifly.dto.Note.SummaryNoteDto;
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return userRepository.findAll(pageable).map(u -> new SummaryUserDto(
                u.getId(),
                u.getName(),
                u.getEmail()
            ));
    }
}
