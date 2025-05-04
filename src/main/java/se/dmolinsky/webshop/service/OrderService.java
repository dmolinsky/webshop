package se.dmolinsky.webshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.dmolinsky.webshop.model.Order;
import se.dmolinsky.webshop.model.OrderLine;
import se.dmolinsky.webshop.model.OrderStatus;
import se.dmolinsky.webshop.model.User;
import se.dmolinsky.webshop.repository.OrderLineRepository;
import se.dmolinsky.webshop.repository.OrderRepository;
import se.dmolinsky.webshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(User user) {
        Order order = new Order(user);
        return order;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(newStatus);
            return orderRepository.save(order);
        }
        return null;
    }

    public List<OrderLine> getOrderLines(Order order) {
        return orderLineRepository.findByOrder(order);
    }


    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(OrderStatus.valueOf(status.name()));
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public void deleteOrderLine(Long orderLineId) {
        orderLineRepository.deleteById(orderLineId);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
