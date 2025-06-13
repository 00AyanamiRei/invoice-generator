package com.makara.invoicegenerator.controller;

import com.makara.invoicegenerator.models.entity.Customer;
import com.makara.invoicegenerator.models.entity.User;
import com.makara.invoicegenerator.models.service.ICustomerService;
import com.makara.invoicegenerator.models.service.IUploadFileService;
import com.makara.invoicegenerator.models.service.IUserService;
import com.makara.invoicegenerator.paginator.PageRender;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@SessionAttributes("client")
public class CustomerController {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private ICustomerService clientService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private IUserService userService;


	@Secured("ROLE_USER")
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> viewImage(@PathVariable String filename) {

		Resource resource = null;

		try {
			resource = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@Secured("ROLE_USER")
	@GetMapping("view/{id}")
	public String viewImage(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Customer customer = clientService.findClientByIdWithInvoices(id);

		if (customer == null) {
			flash.addFlashAttribute("error", "The customer does not exist in the database!");
			return "redirect:/list";
		}

		model.put("customer", customer);
		model.put("title", "Customer Details: " + customer.getFirstName());

		return "view";
	}

//	@RequestMapping(value = {"/list", "/"}, method = RequestMethod.GET)
//	public String list(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
//					   Authentication authentication, Locale locale) {
//
//		if (authentication != null) {
//			logger.info("Hello authenticated user, your username is: ".concat(authentication.getName()));
//		}
//
//		Pageable pageRequest = PageRequest.of(page, 4);
//
//		Page<Customer> clients = clientService.findAll(pageRequest);
//
//		PageRender<Customer> pageRender = new PageRender<>("/list", clients);
//
//		model.addAttribute("title", messageSource.getMessage("text.customer.list.title", null, locale));
//		model.addAttribute("customers", clients);
//		model.addAttribute("page", pageRender);
//
//		return "list";
//	}

	@RequestMapping(value = {"/list"}, method = RequestMethod.GET)
	public String list(@RequestParam(name = "page", defaultValue = "0") int page,
					   Model model, Authentication authentication, Locale locale) {

		User currentUser = userService.findByUsername(authentication.getName());

		Pageable pageable = PageRequest.of(page, 4);
		Page<Customer> customers = clientService.findByUser(currentUser, pageable);
		PageRender<Customer> pageRender = new PageRender<>("/list", customers);

		model.addAttribute("title", messageSource.getMessage("text.customer.list.title", null, locale));
		model.addAttribute("customers", customers);
		model.addAttribute("page", pageRender);

		return "list";
	}


	//	@Secured("ROLE_ADMIN")
	@Secured("ROLE_USER")
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String create(Map<String, Object> model) {
		model.put("title", "Customer Form");

		Customer customer = new Customer();
		model.put("customer", customer);

		return "form";
	}

	//	@Secured("ROLE_ADMIN")
//	@Secured("ROLE_USER")
//	@RequestMapping(value = "/form/{id}")
//	public String edit(@PathVariable Long id, Map<String, Object> model,
//					   RedirectAttributes flash, Authentication authentication) {
//
//		Customer customer = null;
//
//		User currentUser = userService.findByUsername(authentication.getName());
//		if (!customer.getUser().getId().equals(currentUser.getId())) {
//			flash.addFlashAttribute("error", "You do not have permission to access this customer");
//			return "redirect:/list";
//		}
//
//		if (id > 0) {
//			customer = clientService.findOne(id);
//			if (customer == null) {
//				flash.addFlashAttribute("error", "The customer ID does not exist in the database");
//				return "redirect:/list";
//			}
//		} else {
//			flash.addFlashAttribute("error", "The customer ID cannot be zero");
//			return "redirect:/list";
//		}
//		model.put("customer", customer);
//		model.put("title", "Edit Customer");
//		return "form";
//	}
	@Secured("ROLE_USER")
	@RequestMapping(value = "/form/{id}")
	public String edit(@PathVariable Long id, Map<String, Object> model,
					   RedirectAttributes flash, Authentication authentication) {

		if (id <= 0) {
			flash.addFlashAttribute("error", "The customer ID cannot be zero or negative");
			return "redirect:/list";
		}

		Customer customer = clientService.findOne(id);

		if (customer == null) {
			flash.addFlashAttribute("error", "The customer ID does not exist in the database");
			return "redirect:/list";
		}

		User currentUser = userService.findByUsername(authentication.getName());

		if (!customer.getUser().getId().equals(currentUser.getId())) {
			flash.addFlashAttribute("error", "You do not have permission to access this customer");
			return "redirect:/list";
		}

		model.put("customer", customer);
		model.put("title", "Edit Customer");

		return "form";
	}


	//	@Secured("ROLE_ADMIN")
//	@Secured("ROLE_USER")
//	@PostMapping("/form")
//	public String save(@Valid Customer customer, BindingResult result, Model model,
//					   @RequestParam("file") MultipartFile photo, RedirectAttributes flash, SessionStatus status) {
//
//		if (result.hasErrors()) {
//			model.addAttribute("title", "Customer Form");
//			return "form";
//		}
//
//		if (!photo.isEmpty()) {
//
//			if (customer.getId() != null && customer.getId() > 0 && customer.getPhoto() != null
//					&& customer.getPhoto().length() > 0) {
//				uploadFileService.delete(customer.getPhoto());
//			}
//
//			String uniqueFileName = null;
//
//			try {
//				uniqueFileName = uploadFileService.copy(photo);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			flash.addFlashAttribute("info", "You have successfully uploaded '" + uniqueFileName + "'");
//			customer.setPhoto(uniqueFileName);
//		}
//
//		String flashMessage = (customer.getId() != null) ? "Customer successfully edited!" : "Customer successfully created!";
//
//		clientService.save(customer);
//		status.setComplete();
//		flash.addFlashAttribute("success", flashMessage);
//
//		return "redirect:list";
//	}

	@Secured("ROLE_USER")
	@PostMapping("/form")
	public String save(@Valid Customer customer, BindingResult result, Model model,
					   @RequestParam("file") MultipartFile photo,
					   RedirectAttributes flash, SessionStatus status,
					   Authentication authentication) {

		if (result.hasErrors()) {
			model.addAttribute("title", "Customer Form");
			return "form";
		}

		if (!photo.isEmpty()) {
			if (customer.getId() != null && customer.getId() > 0 && customer.getPhoto() != null
					&& customer.getPhoto().length() > 0) {
				uploadFileService.delete(customer.getPhoto());
			}

			String uniqueFileName = null;
			try {
				uniqueFileName = uploadFileService.copy(photo);
			} catch (IOException e) {
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Uploaded: " + uniqueFileName);
			customer.setPhoto(uniqueFileName);
		}

		// Прив’язуємо замовника до поточного користувача
		User currentUser = userService.findByUsername(authentication.getName());
		customer.setUser(currentUser);

		String flashMessage = (customer.getId() != null) ? "Customer updated!" : "Customer created!";
		clientService.save(customer);
		status.setComplete();
		flash.addFlashAttribute("success", flashMessage);

		return "redirect:/list";
	}


	//	@Secured("ROLE_ADMIN")
	@Secured("ROLE_USER")
	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable String id,
						 RedirectAttributes flash,
						 Authentication authentication) {
		Long customerId;
		User currentUser = userService.findByUsername(authentication.getName());

		try {
			customerId = Long.valueOf(id);
		} catch (NumberFormatException e) {
			flash.addFlashAttribute("error", "Invalid customer ID: " + id);
			return "redirect:/list";
		}

		if (customerId > 0) {
			Customer customer = clientService.findOne(customerId);

			if (!customer.getUser().getId().equals(currentUser.getId())) {
				flash.addFlashAttribute("error", "You do not have permission to access this customer");
				return "redirect:/list";
			}

			if (customer == null) {
				flash.addFlashAttribute("error", "The customer does not exist in the database!");
				return "redirect:/list";
			}

			clientService.delete(customerId);
			flash.addFlashAttribute("success", "Customer successfully deleted!");

			if (customer.getPhoto() != null && uploadFileService.delete(customer.getPhoto())) {
				flash.addFlashAttribute("info", "Photo " + customer.getPhoto() + " successfully deleted!");
			}
		}
		return "redirect:/list";
	}
	private boolean hasRole(String role) {

		SecurityContext context = SecurityContextHolder.getContext();

		if (context == null) {
			return false;
		}

		Authentication auth = context.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		for (GrantedAuthority authority : authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info("Hello user " + auth.getName() + ", your role is: " + authority.getAuthority());
				return true;
			}
		}

		return false;
	}
}