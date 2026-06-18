package bilkom.uz.chemical.service;

import bilkom.uz.chemical.dto.grids.GridColumnDto;
import bilkom.uz.chemical.dto.grids.GridConfigDto;
import bilkom.uz.chemical.dto.grids.GridResponse;
import bilkom.uz.chemical.dto.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GridService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JdbcTemplate jdbcTemplate;

    public GridService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Result getByGridId(Long gridId, int page) {
        List<Map<String, Object>> gridRows = jdbcTemplate.queryForList(
            "SELECT view_name, grid_config, grid_columns FROM core.grids WHERE grid_id = ?",
            gridId
        );

        if (gridRows.isEmpty()) {
            return new Result("Grid topilmadi: " + gridId, false, null);
        }

        Map<String, Object> gridRow = gridRows.get(0);
        String viewName = (String) gridRow.get("view_name");

        // PostgreSQL JSONB ustunlari PGobject sifatida keladi, toString() JSON string qaytaradi
        String gridConfigJson  = gridRow.get("grid_config").toString();
        String gridColumnsJson = gridRow.get("grid_columns").toString();

        if (!viewName.matches("[a-zA-Z0-9_.]+")) {
            return new Result("Noto'g'ri view nomi", false, null);
        }

        GridConfigDto gridConfig;
        List<GridColumnDto> gridColumns;
        try {
            gridConfig  = MAPPER.readValue(gridConfigJson, GridConfigDto.class);
            gridColumns = MAPPER.readValue(gridColumnsJson, new TypeReference<List<GridColumnDto>>() {});
        } catch (Exception e) {
            return new Result("Grid konfiguratsiyasini o'qishda xato: " + e.getMessage(), false, null);
        }

        int pageSize = gridConfig.getPage() > 0 ? gridConfig.getPage() : 20;
        int offset   = page * pageSize;

        Long total = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM " + viewName, Long.class
        );
        long totalElements = total != null ? total : 0L;
        int  totalPages    = (int) Math.ceil((double) totalElements / pageSize);

        List<Map<String, Object>> data = jdbcTemplate.queryForList(
            "SELECT * FROM " + viewName + " LIMIT ? OFFSET ?",
            pageSize, offset
        );

        GridResponse response = new GridResponse(gridConfig, gridColumns, data, totalElements, totalPages, page);
        return new Result("Muvaffaqiyatli", true, response);
    }
}
