package bilkom.uz.chemical.dto.employees;

import bilkom.uz.chemical.entity.employees.EmployeeState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private EmployeeState state;
    private String email;
    private String address;
    private String inps;
    private String pasportCode;
    private String photo;
}
