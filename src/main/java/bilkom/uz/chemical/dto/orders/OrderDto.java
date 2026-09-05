package bilkom.uz.chemical.dto.orders;

import bilkom.uz.chemical.entity.orders.OrderState;
import bilkom.uz.chemical.entity.orders.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long clientId;
    private String orderDescription;
    private OrderStatus status;
    private OrderState state;
    private List<OrderItemDto> items;
}
