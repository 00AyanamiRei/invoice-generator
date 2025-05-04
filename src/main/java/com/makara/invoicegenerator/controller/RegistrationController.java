package com.makara.invoicegenerator.controller;

import com.makara.invoicegenerator.models.entity.User;
import com.makara.invoicegenerator.models.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(false); // Set user as inactive by default

        // Save user and assign default role
        userService.save(user);
        userService.assignRole(user.getUsername(), "ROLE_USER");

        model.addAttribute("success", "Registration successful! Please wait for admin approval.");
        return "login";
    }
}