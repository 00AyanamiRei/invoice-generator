package com.makara.invoicegenerator.controller;

import com.makara.invoicegenerator.models.entity.Role;
import com.makara.invoicegenerator.models.entity.User;
import com.makara.invoicegenerator.models.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit-roles/{id}")
    public String editRoles(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        List<Role> roles = user.getRoles() != null ? user.getRoles() : new ArrayList<>();
        List<String> roleNames = roles.stream()
                .map(Role::getAuthority)
                .collect(Collectors.toList());

        // Debug logs
        System.out.println("All roles: " + userService.findAllRoles());
        System.out.println("User roles: " + roleNames);

        model.addAttribute("user", user);
        model.addAttribute("roles", userService.findAllRoles());
        model.addAttribute("userRoleNames", roleNames);
        return "admin/edit-roles";
    }

  @PostMapping("/edit-roles")
  public String updateRoles(
          @RequestParam Long userId,
          @RequestParam(value = "roles", required = false) List<String> roles,
          RedirectAttributes redirectAttributes) {

      if (roles == null || roles.isEmpty()) {
          redirectAttributes.addFlashAttribute("errorMessage", "You must select at least one role.");
          return "redirect:/admin/edit-roles/" + userId;
      }

      userService.updateRoles(userId, roles);
      return "redirect:/admin/users";
  }
}