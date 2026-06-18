package bilkom.uz.chemical.dto.grids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GridResponse {
    private GridConfigDto gridConfig;
    private List<GridColumnDto> gridColumns;
    private List<Map<String, Object>> data;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
