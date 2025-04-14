package se.dmolinsky.webshop;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String showProductPage(@PathVariable("id") Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);

        Optional<Product> product = productService.getProductById(id);

        if (product.isPresent()) {
            Product p = product.get();
            if (p.getImage() != null) {
                p.setBase64Image();
            }
            model.addAttribute("product", p);
        } else {
            model.addAttribute("error", "Product not found");
        }
        return "product";
    }


    @GetMapping("/admin-products")
    public String showAdminPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        if (!user.getRole().equals("ADMIN")) {
            return "redirect:/index";
        }
        model.addAttribute("user", user);


        model.addAttribute("product", new Product());
        model.addAttribute("categories", Category.values());
        model.addAttribute("products", productService.getAllProducts());

        return "admin-products";
    }

    @PostMapping("/admin-add-products")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             Model model,
                             @RequestParam("uploadedimage") MultipartFile imageFile) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println("Error: " + error.getDefaultMessage()));

            return "admin-products";
        }

        if (imageFile.isEmpty()) {
            model.addAttribute("error", "No image file selected");
            return "admin-products";
        }

        try {
            byte[] imageBytes = imageFile.getBytes();
            product.setImage(imageBytes);
        } catch (IOException e) {
            model.addAttribute("error", "Error while uploading the image.");
            return "admin-products";
        }

        productService.addProduct(product);
        return "redirect:/index";
    }

}
