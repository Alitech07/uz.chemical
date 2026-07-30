package bilkom.uz.chemical.dto.grids;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GridConfigDto {
    private int page;
    private boolean search;
    private boolean filter;
    private boolean checkbox;
    private boolean showFile;
    private boolean showFooter;
    private List<GridGroupDto> groups;
}
