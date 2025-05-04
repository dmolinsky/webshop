package se.dmolinsky.webshop.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {}

    public Order(User user) {
        this.orderLines = new ArrayList<OrderLine>();
        this.user = user;
        this.status = OrderStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public void addOrderLine(OrderLine orderLine) {
        orderLines.add(orderLine);
    }

    public void removeOrderLineByProductId(Long productId) {
        orderLines.removeIf(orderLine -> orderLine.getProduct().getId().equals(productId));
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getTotalAmount() {
        double total = 0.0;
        for (OrderLine line : this.getOrderLines()) {
            Product product = line.getProduct();
            int quantity = line.getQuantity();
            total += product.getPrice() * quantity;
        }
        return total;
    }

    public String getFormattedProductList() {
        StringBuilder productList = new StringBuilder();
        for (OrderLine line : this.getOrderLines()) {
            Product product = line.getProduct();
            int quantity = line.getQuantity();
            productList.append("- ")
                    .append(product.getName())
                    .append(" (")
                    .append(quantity)
                    .append(" st)\n");
        }
        return productList.toString();
    }
}
