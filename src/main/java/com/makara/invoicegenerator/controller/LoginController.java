package com.makara.invoicegenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout, Model model, Principal principal,
                        RedirectAttributes flash) {

        if (principal != null) { // if already logged in
            flash.addFlashAttribute("info", "You have already logged in");
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error",
                    "Login error: Incorrect username or password, please try again!");
        }

        if (logout != null) {
            model.addAttribute("success", "You have successfully logged out!");
        }

        return "login";
    }
}