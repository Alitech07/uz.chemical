package bilkom.uz.chemical.repository.orders;

import bilkom.uz.chemical.entity.orders.Order;
import bilkom.uz.chemical.entity.orders.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClientId(Long clientId);
    List<Order> findAllByStatus(OrderStatus status);
}
