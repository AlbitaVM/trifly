package es.albavm.tfg.trifly.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import es.albavm.tfg.trifly.Model.User;
import es.albavm.tfg.trifly.Service.UserService;


@Controller
public class UserController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    @GetMapping("/login")
    public String showbLogin(@RequestParam(value = "error", required = false) String error,Model model) {
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

        if(userService.existsByEmail(user.getEmail())){
             model.addAttribute("error", "Este email ya está registrado");
            return "register";
        }else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            userService.save(user);
             return "redirect:/login";
        }
       
    }
    
}
