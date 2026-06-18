package bilkom.uz.chemical.dto.products;

import bilkom.uz.chemical.entity.products.ProductState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String productName;
    private String composition;
    private String measure;
    private String countryManufacture;
    private String manufacturerName;
    private ProductState state;
}
