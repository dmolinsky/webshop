package se.dmolinsky.webshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.dmolinsky.webshop.model.EmailService;
import se.dmolinsky.webshop.service.OrderService;
import se.dmolinsky.webshop.service.ProductService;
import se.dmolinsky.webshop.model.*;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SessionData sessionData;

    @GetMapping("/createOrder")
    public String createOrderManually() {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }
        sessionData.setOrder(orderService.createOrder(user));
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String cartPage(Model model) {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        Order order = sessionData.getOrder();
        model.addAttribute("orderLines", order.getOrderLines());
        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("redirectTo") String redirectTo,
                            @RequestParam("quantity") Long quantity) {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Product> productOpt = productService.getProductById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            Order order = sessionData.getOrder();

            if (order == null) {
                order = new Order();
                sessionData.setOrder(order);
            }

            boolean productExistsInCart = false;
            for (OrderLine orderLine : order.getOrderLines()) {
                if (orderLine.getProduct().getId().equals(productId)) {
                    orderLine.setQuantity(orderLine.getQuantity() + Math.toIntExact(quantity));
                    productExistsInCart = true;
                    break;
                }
            }

            if (!productExistsInCart) {
                OrderLine orderLine = new OrderLine(order, product, Math.toIntExact(quantity));
                order.addOrderLine(orderLine);
            }

            sessionData.setOrder(order);
        }

        if ("product".equals(redirectTo)) {
            return "redirect:/product/" + productId;
        } else {
            return "redirect:/index";
        }
    }

    @PostMapping("/update-product-quantity")
    public String updateProductQuantity(@RequestParam("productId") Long productId,
                                        @RequestParam("quantity") int quantity) {
        Order order = sessionData.getOrder();
        for (OrderLine line : order.getOrderLines()) {
            if (line.getProduct().getId().equals(productId)) {
                line.setQuantity(quantity);
                break;
            }
        }
        return "redirect:/cart";
    }



    @PostMapping("/remove-product-from-cart")
    public String removeProductFromCart(@RequestParam("productId") Long productId) {
        Order order = sessionData.getOrder();

        order.removeOrderLineByProductId(productId);

        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Model model) {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        Order order = sessionData.getOrder();

        try {
            order.setStatus(OrderStatus.PENDING);
            orderService.saveOrder(order);

            String email = user.getEmail();
            String orderDetails = "Ordernummer: " + order.getId() + ": \n" +
                    order.getFormattedProductList() + "\nTotal: " + order.getTotalAmount() + "kr";
            emailService.sendOrderConfirmation(email, orderDetails);

            sessionData.setOrder(new Order(user));
            return "redirect:/order-successful?orderId=" + order.getId();
        } catch (Exception e) {
            return "redirect:/order-unsuccessful";
        }

    }

    @GetMapping("/order-successful")
    public String orderSuccessful(@RequestParam("orderId") Long orderId, Model model) {
        User user = sessionData.getUser();
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "order-successful";
        } else {
            return "order-unsuccessful";
        }
    }

    @GetMapping("/admin-orders")
    public String showAllOrders(Model model) {

        User user = sessionData.getUser();

        if (user == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/index";
        }

        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders", orders);
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "admin-orders";
    }

    @PostMapping("/admin-update-order")
    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
                                    @RequestParam("status") OrderStatus status) {
        Optional<Order> orderOpt = orderService.getOrderById(orderId);
        orderOpt.ifPresent(order -> {
            order.setStatus(status);
            orderService.saveOrder(order);
        });
        return "redirect:/admin-orders";
    }


}
