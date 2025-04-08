package se.dmolinsky.webshop;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/createOrder")
    public void createOrder(HttpSession session, User user) {
        if (session.getAttribute("order") == null) {
            Order order = orderService.createOrder(user);
            session.setAttribute("order", order);
        }
    }

    @GetMapping("/cart")
    public String cartPage(HttpSession session, Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        Order order = (Order) session.getAttribute("order");

        List<OrderLine> orderLines = order.getOrderLines();

        model.addAttribute("orderLines", orderLines);

        return "cart";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("redirectTo") String redirectTo,
                            @RequestParam("quantity") Long quantity,
                            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        Optional<Product> productOpt = productService.getProductById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();

            Order order = (Order) session.getAttribute("order");

            if (order == null) {
                order = new Order();
                session.setAttribute("order", order);
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

            session.setAttribute("order", order);
        }

        if ("product".equals(redirectTo)) {
            return "redirect:/product/" + productId;
        } else {
            return "redirect:/index";
        }
    }

}
