package se.dmolinsky.webshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.dmolinsky.webshop.model.Category;
import se.dmolinsky.webshop.model.Product;
import se.dmolinsky.webshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<List<Product>> searchProducts(String name, Category category) {
        List<Product> products;

        if (name != null && !name.isEmpty()) {
            products = productRepository.findByName(name);
        } else if (category != null && !category.getCategoryName().isEmpty()) {
            products = productRepository.findByCategory(category.getCategoryName());
        } else {
            return Optional.empty();
        }

        products.forEach(product -> {
            product.setBase64Image();
        });


        return products.isEmpty() ? Optional.empty() : Optional.of(products);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
