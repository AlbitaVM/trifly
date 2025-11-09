package es.albavm.tfg.trifly.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String showbLogin() {
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
            return "/register";
        }else{
            user.setEmail(user.getEmail());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
             return "redirect:/login";
        }
       
    }
    
}
