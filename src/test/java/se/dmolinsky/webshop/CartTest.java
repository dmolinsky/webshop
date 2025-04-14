package se.dmolinsky.webshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartTest {
    @Mock
    OrderLine orderLine;

    @Mock
    Product product;

    private Order order;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
    }

    @Test
    public void testAddProductToCart() {

        product.setName("Test product");
        product.setPrice(10.00);
        product.setCategory(Category.PLANTS.getCategoryName());

        when(orderLine.getProduct()).thenReturn(product);

        order.addOrderLine(orderLine);

        assertFalse(order.getOrderLines().isEmpty());
        assertTrue(order.getOrderLines().getFirst().getProduct().equals(product));
    }

}
