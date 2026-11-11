package bilkom.uz.chemical.repository.orders;

import bilkom.uz.chemical.entity.orders.Order;
import bilkom.uz.chemical.entity.orders.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClientId(Long clientId);
    List<Order> findAllByStatus(OrderStatus status);

    long countByCreatedById(Long userId);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createdBy.id = :userId")
    BigDecimal sumAmountByCreatedById(@Param("userId") Long userId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdBy.id = :userId AND MONTH(o.createdOn) = :month AND YEAR(o.createdOn) = :year")
    long countByCreatedByIdAndMonth(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.createdBy.id = :userId AND MONTH(o.createdOn) = :month AND YEAR(o.createdOn) = :year")
    BigDecimal sumAmountByCreatedByIdAndMonth(@Param("userId") Long userId, @Param("month") int month, @Param("year") int year);
}
