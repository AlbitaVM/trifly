package es.albavm.tfg.trifly.Security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import es.albavm.tfg.trifly.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import es.albavm.tfg.trifly.Model.User;


@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{
     @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Obtener email del usuario que ha iniciado sesión
        String email = authentication.getName();

        // Buscar usuario y actualizar lastLoginDate
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setLastLoginDate(LocalDateTime.now());
            userRepository.saveAndFlush(user);
            System.out.println("Usuario logueado: " + email);
System.out.println("LastLogin antes: " + user.getLastLoginDate());
user.setLastLoginDate(LocalDateTime.now());
userRepository.saveAndFlush(user);
System.out.println("LastLogin después: " + user.getLastLoginDate());
        }

        // Redirigir al home (o a la URL por defecto)
        response.sendRedirect("/");
    }
}
