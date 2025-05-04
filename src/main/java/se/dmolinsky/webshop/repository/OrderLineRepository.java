package se.dmolinsky.webshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.dmolinsky.webshop.model.Order;
import se.dmolinsky.webshop.model.OrderLine;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    List<OrderLine> findByOrder(Order order);

}
