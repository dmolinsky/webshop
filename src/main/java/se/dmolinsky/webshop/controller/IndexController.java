package se.dmolinsky.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.dmolinsky.webshop.service.ProductService;
import se.dmolinsky.webshop.model.SearchCriteria;
import se.dmolinsky.webshop.model.Category;
import se.dmolinsky.webshop.model.Product;
import se.dmolinsky.webshop.model.SessionData;
import se.dmolinsky.webshop.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SessionData sessionData;

    @GetMapping("/index")
    public String indexPage(Model model) {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("categories", Category.values());
        model.addAttribute("searchCriteria", new SearchCriteria());

        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);

        return "index";
    }

    @PostMapping("/index")
    public String searchProducts(@ModelAttribute SearchCriteria searchCriteria, Model model) {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        Optional<List<Product>> foundProducts = productService.searchProducts(
                searchCriteria.getName(),
                searchCriteria.getCategory()
        );

        if (foundProducts.isPresent()) {
            model.addAttribute("products", foundProducts.get());
        } else {
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("message", "No products matching your search criteria was found.");
        }

        model.addAttribute("categories", Category.values());
        model.addAttribute("searchCriteria", searchCriteria);

        return "index";
    }
}
