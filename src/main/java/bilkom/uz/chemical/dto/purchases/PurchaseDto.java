package bilkom.uz.chemical.dto.purchases;

import bilkom.uz.chemical.entity.purchases.PurchaseState;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long supplierId;
    private String country;
    private String brand;
    private Long deliveryTypeId;
    private Long paymentTypeId;
    private Long currencyId;
    private String certificatePath;
    private String purchaseDescription;
    private BigDecimal transportExpenses;
    private BigDecimal customsExpenses;
    private PurchaseStatus status;
    private PurchaseState state;
    private List<PurchaseItemDto> items;
}
