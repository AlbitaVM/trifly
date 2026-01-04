package es.albavm.tfg.trifly.Controller;

import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Service.UserService;
import es.albavm.tfg.trifly.dto.Itinerary.EditItineraryDto;
import es.albavm.tfg.trifly.dto.Note.EditNoteDto;
import es.albavm.tfg.trifly.dto.User.EditProfileDto;
import es.albavm.tfg.trifly.dto.User.ProfileDto;

@Controller
public class UserController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showbLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showbRegister() {
        return "register";
    }

    @PostMapping("/signup")
    public String signup(User user, Model model) {

        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Este email ya está registrado");
            return "register";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userService.save(user);
            return "redirect:/login";
        }

    }

    @GetMapping("/user/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws Exception {
        User user = userService.findById(id).orElseThrow();

        byte[] imageBytes;
        MediaType mediaType;

        if (user.getImageFile() != null) {
            imageBytes = user.getImageFile().getBytes(1, (int) user.getImageFile().length());
            mediaType = MediaType.IMAGE_PNG;
        } else {
            Resource resource = new ClassPathResource("static/img/users/default_photo.png");
            try (InputStream is = resource.getInputStream()) {
                imageBytes = is.readAllBytes();
            }
            mediaType = MediaType.IMAGE_PNG;
        }
        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imageBytes);
    }

    @ModelAttribute
    private void addUserInfoToModel(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("isAuthenticated", true);

            String email = principal.getName();
            Optional<User> optionalUser = userService.findByEmail(email);

            if (optionalUser.isPresent()) {
                model.addAttribute("userName", optionalUser.get().getName());
                model.addAttribute("userId", optionalUser.get().getId());
            }
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {

        ProfileDto profile = userService.getProfile(principal.getName());
        model.addAttribute("isProfile", true);
        model.addAttribute("user", profile);
        return "/profile";
    }

    @GetMapping("/profile/{id}/edit")
    public String showFormEdit(Principal principal, Model model) {
        String email = principal.getName();
        EditProfileDto user = userService.getProfileForEdit(email);
        model.addAttribute("user", user);
        model.addAttribute("email", user.getEmail());
        return "/edit-profile";
    }

    @PostMapping("/profile/{id}/edit")
    public String editProfile(Principal principal, Model model, @ModelAttribute EditProfileDto dto,
            BindingResult bindingResult, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        String email = principal.getName();
        try {
            userService.updateProfile(email, dto, imageFile);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error", e.getMessage()); // Captura el mensaje de error
        }

        if (bindingResult.hasErrors()) {
            // Rellenar campos obligatorios que no vienen del formulario
            EditProfileDto filledDto = new EditProfileDto(
                    dto.getId(),
                    dto.getName(),
                    userService.getProfileForEdit(principal.getName()).getEmail() // asegura que email nunca sea null
            );
            filledDto.setCurrentPassword(dto.getCurrentPassword());
            filledDto.setNewPassword(dto.getNewPassword());
            filledDto.setConfirmPassword(dto.getConfirmPassword());

            model.addAttribute("user", filledDto);
            return "/edit-profile";
        }
        return "redirect:/profile";
    }
}
