package se.dmolinsky.webshop;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @GetMapping("/index")
    public String indexPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        model.addAttribute("categories", Category.values());
        model.addAttribute("searchCriteria", new SearchCriteria());

        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);

        return "index";
    }

    @PostMapping("/index")
    public String searchProducts(@ModelAttribute SearchCriteria searchCriteria, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        model.addAttribute("products", new ArrayList<Product>());

        Optional<List<Product>> foundProducts = productService.searchProducts(searchCriteria.getName(), searchCriteria.getCategory());


        if (foundProducts.isPresent()) {
            model.addAttribute("products", foundProducts.get());
        } else {
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("message", "No products matching your search criteria was found.");
        }

        model.addAttribute("categories", Category.values());

        model.addAttribute("searchCriteria", searchCriteria); //Reset values in inputfields

        return "index";
    }
}
