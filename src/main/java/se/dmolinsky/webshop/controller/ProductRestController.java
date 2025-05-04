package se.dmolinsky.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.dmolinsky.webshop.service.ProductService;
import se.dmolinsky.webshop.model.Category;
import se.dmolinsky.webshop.model.Product;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(product.get());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Category category) {
        Optional<List<Product>> foundProducts = productService.searchProducts(name, category);

        if (foundProducts.isPresent()) {
            return ResponseEntity.ok(foundProducts.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<Product> updateProductPrice(@PathVariable Long id, @RequestParam double price) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setPrice(price);
            return ResponseEntity.ok(productService.addProduct(product));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productService.getProductById(id);
        if (productOptional.isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
