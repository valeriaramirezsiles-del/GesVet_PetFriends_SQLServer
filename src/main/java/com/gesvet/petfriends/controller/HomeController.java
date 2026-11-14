package com.gesvet.petfriends.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/panel")
    public String panel(Authentication auth, Model model) {
        boolean admin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("usuario", auth.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("rol", admin ? "Administrador" : "Médico Veterinario");
        return "panel";
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/citas")
    public String citas() {
        return "citas";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }
}
