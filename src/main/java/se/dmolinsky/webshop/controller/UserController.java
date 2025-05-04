package se.dmolinsky.webshop.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.dmolinsky.webshop.service.OrderService;
import se.dmolinsky.webshop.service.UserService;
import se.dmolinsky.webshop.model.LoginForm;
import se.dmolinsky.webshop.model.SessionData;
import se.dmolinsky.webshop.model.User;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public OrderController orderController;

    @Autowired
    public OrderService orderService;

    @Autowired
    public SessionData sessionData;

    @GetMapping("/login")
    String loginPage(Model model) {
        if (sessionData.getUser() != null) {
            return "redirect:/index";
        }
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            String messages = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                messages += fieldError.getDefaultMessage() + "*";
            }
            model.addAttribute("errormessage", messages);
            return "login";
        }

        Optional<User> user = userService.getByUsername(form.getUsername());
        if (user.isPresent() && userService.checkPassword(form.getPassword(), user.get().getPassword())) {
            sessionData.setUser(user.get());
            sessionData.setOrder(orderService.createOrder(user.get()));
            return "redirect:/index";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    String registerUserPage(Model model) {
        if (sessionData.getUser() != null) {
            return "redirect:/logout";
        }
        model.addAttribute("registerForm", new User());
        return "register";
    }

    @PostMapping("/register")
    String registerUser(Model model, @Valid @ModelAttribute("registerForm") User user, BindingResult bindingResult) {

        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("errormessage", "Username is already in use");
            return "register";
        }

        if (bindingResult.hasErrors()) {
            String messages = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                messages += fieldError.getDefaultMessage() + "*";
            }
            model.addAttribute("errormessage", messages);
            return "register";
        }

        String hashedPassword = userService.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("USER");
        userService.add(user);
        sessionData.setUser(user);
        sessionData.setOrder(orderService.createOrder(user));

        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


}
