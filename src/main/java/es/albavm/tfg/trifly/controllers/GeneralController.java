package es.albavm.tfg.trifly.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
     @GetMapping("/")
    public String showIndex() {
        return "index"; // buscará templates/index.html
    }
}
