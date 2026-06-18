package bilkom.uz.chemical.dto.grids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GridColumnDto {
    private String key;
    private String label;
    private String type;
    private boolean exportable;
    private String width;
    private Map<String, String> badgeMap;
}
