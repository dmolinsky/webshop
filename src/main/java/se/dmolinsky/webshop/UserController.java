package se.dmolinsky.webshop;

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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderController orderController;

    @Autowired
    private OrderService orderService;

    @GetMapping("/login")
    String loginPage(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/index";
        }
        model.addAttribute("user", user);

        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, Model model, HttpSession session) {

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
            session.setAttribute("user", user.get());

            orderController.createOrder(session, user.get());

            return "redirect:/index";
        } else {
            System.out.println(form.getPassword());
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    String registerUserPage(Model model, HttpSession session) {

        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            return "redirect:/logout";
        }
        model.addAttribute("user", sessionUser);

        model.addAttribute("registerForm", new User());

        return "register";
    }

    @PostMapping("/register")
    String registerUser(Model model, @Valid @ModelAttribute("registerForm") User user, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", null);
            String messages = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                messages += fieldError.getDefaultMessage() +"*";
            }
            model.addAttribute("errormessage", messages);
            return "register";

        } else {
            String hashedPassword = userService.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            user.setRole("USER");
            userService.add(user);
            session.setAttribute("user", user);
            orderController.createOrder(session, user);

            return "index";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


}
