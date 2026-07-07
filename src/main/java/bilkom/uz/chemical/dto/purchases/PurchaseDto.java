package bilkom.uz.chemical.dto.purchases;

import bilkom.uz.chemical.entity.purchases.PurchaseState;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long supplierId;
    private String productName;
    private String country;
    private String brand;
    private String deliveryType;
    private String certificatePath;
    private String purchaseDescription;
    private BigDecimal price;
    private BigDecimal amount;
    private PurchaseStatus status;
    private PurchaseState state;
}
