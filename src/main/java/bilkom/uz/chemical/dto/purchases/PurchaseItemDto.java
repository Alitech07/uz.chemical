package bilkom.uz.chemical.dto.purchases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItemDto {
    private Long productId;
    private BigDecimal price;
    private BigDecimal amount;
}
