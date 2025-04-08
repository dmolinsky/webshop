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
import java.util.Base64;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public String showProductPage(@PathVariable("id") Long id, Model model) {
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


    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null || !user.getRole().equals("ADMIN")) {
            return "redirect:/login";
        }

        model.addAttribute("product", new Product());
        return "admin";
    }

    @PostMapping("/admin")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             Model model,
                             @RequestParam("uploadedimage") MultipartFile imageFile) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println("Error: " + error.getDefaultMessage()));

            return "admin";
        }

        if (imageFile.isEmpty()) {
            model.addAttribute("error", "No image file selected");
            return "admin";
        }

        try {
            byte[] imageBytes = imageFile.getBytes();
            product.setImage(imageBytes);
        } catch (IOException e) {
            model.addAttribute("error", "Error while uploading the image.");
            return "admin";
        }

        productService.addProduct(product);
        return "redirect:/index";
    }
}
