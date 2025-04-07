package se.dmolinsky.webshop;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/createOrder")
    public void createOrder(HttpSession session, User user) {
        if (session.getAttribute("order") == null) {
            Order order = orderService.createOrder(user);
            session.setAttribute("order", order);
        }
    }
}
