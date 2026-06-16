package bilkom.uz.chemical.dto.warehouse;

import bilkom.uz.chemical.entity.warehouse.WarehouseState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDto {
    private Long productId;
    private Double residual;
    private String measure;
    private String storageSpace;
    private WarehouseState state;
    private Long createdById;
}
