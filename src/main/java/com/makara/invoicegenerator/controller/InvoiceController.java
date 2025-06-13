package com.makara.invoicegenerator.controller;

import com.makara.invoicegenerator.models.entity.*;
import com.makara.invoicegenerator.models.service.ICustomerService;
import javax.validation.Valid;

import com.makara.invoicegenerator.models.service.IProductService;
import com.makara.invoicegenerator.models.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Secured("ROLE_USER")
@Controller
@RequestMapping(value = "/invoice")
@SessionAttributes("invoice")
public class InvoiceController {

    @Autowired
    private ICustomerService clientService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IProductService productService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/view/{id}")
    public String view(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

        Invoice invoice = clientService.findInvoiceByIdWithClientWithInvoiceItemsWithProduct(id);

        if (invoice == null) {
            flash.addFlashAttribute("error", "Error: The invoice does not exist in the database");
            return "redirect:/list";
        }

        model.addAttribute("invoice", invoice);
        model.addAttribute("title", "Invoice: ".concat(invoice.getDescription()));

        return "invoice/view";
    }

    @GetMapping("/form/{clientId}")
    public String create(@PathVariable(value = "clientId") Long clientId, Model model, RedirectAttributes flash) {

        Customer customer = clientService.findOne(clientId);

        if (customer == null) {
            flash.addFlashAttribute("error", "The customer does not exist in the database");
            return "redirect:/list";
        }

        Invoice invoice = new Invoice();
        invoice.setClient(customer);

        model.addAttribute("invoice", invoice);
        model.addAttribute("title", "Create Invoice");

        return "invoice/form";
    }

//    @GetMapping(value = "/load-products/{term}", produces = { "application/json" })
//    public @ResponseBody List<Product> loadProducts(@PathVariable String term) {
//        return clientService.findProductByName(term);
//    }

    @PostMapping("/form")
    public String save(@Valid Invoice invoice, BindingResult result, Model model,
                       @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                       @RequestParam(name = "quantity[]", required = false) Integer[] quantity, RedirectAttributes flash,
                       SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("title", "Create Invoice");
            return "invoice/form";
        }

        if (itemId == null || itemId.length == 0) {
            model.addAttribute("title", "Create Invoice");
            model.addAttribute("error", "Error: The invoice must contain at least one line");
            flash.addFlashAttribute("error", "Error: The invoice must contain at least one line");
            return "invoice/form";
        }

        for (int i = 0; i < itemId.length; i++) {
            Product product = clientService.findProductById(itemId[i]);

            ItemInvoice line = new ItemInvoice();
            line.setQuantity(quantity[i]);
            line.setProduct(product);
            invoice.addItemInvoice(line);

            log.info("ID: " + itemId[i].toString() + ", quantity: " + quantity[i].toString());
        }

        clientService.saveInvoice(invoice);
        status.setComplete();

        flash.addFlashAttribute("success", "Invoice created successfully!");

        return "redirect:/view/" + invoice.getClient().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id, RedirectAttributes flash) {

        Invoice invoice = clientService.findInvoiceById(id);

        if (invoice != null) {
            clientService.deleteInvoice(id);
            flash.addFlashAttribute("success", "Invoice deleted successfully!");
            return "redirect:/view/" + invoice.getClient().getId();
        }

        flash.addFlashAttribute("error", "The invoice does not exist in the database, it could not be deleted!");
        return "redirect:/list";
    }

    @GetMapping(value = "/load-products/{term}", produces = "application/json")
    @ResponseBody
    public List<Product> loadProducts(@PathVariable String term, Authentication authentication) {
        User currentUser = userService.findByUsername(authentication.getName());
        return productService.findByNameAndUser(term, currentUser);
    }

}