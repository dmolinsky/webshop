package se.dmolinsky.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Base64;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters")
    private String name;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @Size(max = 100, message = "Category should not exceed 100 characters")
    private String category;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Lob
    @Column(name = "base64image", columnDefinition = "LONGTEXT")
    private String base64Image;

    public Product() {}

    public Product(String name, Double price, String category, byte[] image) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
    }

    public Product(String name, Double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
        setBase64Image();
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image() {
        this.base64Image = (image != null) ? Base64.getEncoder().encodeToString(image) : null;
    }

}
