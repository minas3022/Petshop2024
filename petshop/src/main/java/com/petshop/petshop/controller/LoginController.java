package com.petshop.petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciais inválidas, tente novamente.");
        }
        if (logout != null) {
            model.addAttribute("message", "Logout realizado com sucesso.");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "logout"; // Return the name of the view for the logout page
    }
}