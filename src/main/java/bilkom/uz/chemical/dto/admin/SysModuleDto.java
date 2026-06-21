package bilkom.uz.chemical.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysModuleDto {
    private String name;
    private String label;
    private String route;
    private Long sectionId;
    private Integer orderNum;
    private String state;
}
