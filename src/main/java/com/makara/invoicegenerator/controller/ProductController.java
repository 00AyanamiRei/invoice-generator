package com.makara.invoicegenerator.controller;

import com.makara.invoicegenerator.models.dao.IProductDao;
import com.makara.invoicegenerator.models.entity.Product;
import com.makara.invoicegenerator.models.entity.User;
import com.makara.invoicegenerator.models.service.IProductService;
import com.makara.invoicegenerator.models.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
@Secured("ROLE_USER")
public class ProductController {

    @Autowired
    private IProductDao productDao;

    @Autowired
    private IUserService userService;


    @GetMapping
    public String list(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        List<Product> products = productDao.findByUser(user);
        model.addAttribute("products", products);
        model.addAttribute("title", "My Products");
        return "products/list";
    }

    @GetMapping("/form")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Add Product");
        return "products/form";
    }

    @PostMapping("/form")
    public String save(@Valid Product product, BindingResult result,
                       Authentication authentication,
                       RedirectAttributes flash) {

        if (result.hasErrors()) {
            return "products/form";
        }

        User user = userService.findByUsername(authentication.getName());
        product.setUser(user);

        productDao.save(product);
        flash.addFlashAttribute("success", "Product saved!");

        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,
                       Authentication authentication, RedirectAttributes flash) {
        Product product = productDao.findById(id).orElse(null);

        User user = userService.findByUsername(authentication.getName());
        if (product == null || !product.getUser().getId().equals(user.getId())) {
            flash.addFlashAttribute("error", "Access denied");
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        model.addAttribute("title", "Edit Product");
        return "products/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication,
                         RedirectAttributes flash) {
        Product product = productDao.findById(id).orElse(null);
        User user = userService.findByUsername(authentication.getName());

        if (product == null || !product.getUser().getId().equals(user.getId())) {
            flash.addFlashAttribute("error", "Access denied");
            return "redirect:/products";
        }

        productDao.delete(product);
        flash.addFlashAttribute("success", "Product deleted");
        return "redirect:/products";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName().equals("anonymousUser")) return null;
        return userService.findByUsername(auth.getName()); // або через UserDetailsService
    }
}
