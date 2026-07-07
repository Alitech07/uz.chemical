package bilkom.uz.chemical.dto.suppliers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private Long id;
    private String freightForwarder;
    private String legalName;
    private String brandName;
    private String typeOfActivity;
    private String contactPerson;
    private String phone;
    private String email;
    private String address;
    private String isActive;
}
