package se.dmolinsky.webshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<OrderLine> orderLines = new ArrayList<>();
        Order order = new Order(orderLines, user);
        return orderRepository.save(order);
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

    public List<OrderLine> getOrderLinesByOrder(Long orderId) {
        return orderLineRepository.findByOrderId(orderId);
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
}
