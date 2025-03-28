package se.dmolinsky.webshop;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    String loginPage(Model model) {

        //OM USER ÄR LOGGED IN REDAN: return "redirect:/index"; // eller vad nästa sida heter
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            System.out.println("går in här");
            String messages = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                messages += fieldError.getDefaultMessage() + "*";
            }
            model.addAttribute("errormessage", messages);
            return "login";
        }

        Optional<User> user = userService.getByUsername(form.getUsername());
        if (user.isPresent() && passwordEncoder.matches(form.getPassword(), user.get().getPassword())) {
            session.setAttribute("user", user.get());
            return "redirect:/index";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }


    @GetMapping("/register")
    String registerUserPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    String registerUser(Model model, @Valid User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String messages = "";
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                messages += fieldError.getDefaultMessage() +"*";
            }
            model.addAttribute("errormessage", messages);
            return "register";

        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.add(user);
            return "register"; //ska vara nästa sida när den implementerats
        }

    }




}
