package bilkom.uz.chemical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolesDto {
    private Long id;
    private String name;
    private String description;
    private Set<Long> permissionIds;
    private Set<Long> moduleIds;
}
