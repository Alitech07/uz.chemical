package bilkom.uz.chemical.service;

import bilkom.uz.chemical.dto.grids.GridColumnDto;
import bilkom.uz.chemical.dto.grids.GridConfigDto;
import bilkom.uz.chemical.dto.grids.GridResponse;
import bilkom.uz.chemical.dto.Result;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GridService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JdbcTemplate jdbcTemplate;

    public GridService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Result getByGridId(Long gridId, int page, Map<String, String> filters) {
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

        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        if (filters != null && !filters.isEmpty()) {
            Set<String> viewColumns = viewColumns(viewName);
            for (Map.Entry<String, String> f : filters.entrySet()) {
                String col = f.getKey();
                if (!viewColumns.contains(col.toLowerCase())) {
                    return Result.warn("Grid view'da '" + col + "' ustuni yo'q (" + viewName + ")");
                }
                where.append(where.length() == 0 ? " WHERE " : " AND ")
                     .append("CAST(\"").append(col.toLowerCase()).append("\" AS text) = ?");
                args.add(f.getValue());
            }
        }

        Long total = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM " + viewName + where, Long.class, args.toArray()
        );
        long totalElements = total != null ? total : 0L;
        int  totalPages    = (int) Math.ceil((double) totalElements / pageSize);

        List<Object> pageArgs = new ArrayList<>(args);
        pageArgs.add(pageSize);
        pageArgs.add(offset);
        List<Map<String, Object>> data = jdbcTemplate.queryForList(
            "SELECT * FROM " + viewName + where + " LIMIT ? OFFSET ?",
            pageArgs.toArray()
        );

        GridResponse response = new GridResponse(gridConfig, gridColumns, data, totalElements, totalPages, page);
        return new Result("Muvaffaqiyatli", true, response);
    }

    private Set<String> viewColumns(String viewName) {
        return jdbcTemplate.query("SELECT * FROM " + viewName + " LIMIT 0", rs -> {
            ResultSetMetaData md = rs.getMetaData();
            Set<String> cols = new HashSet<>();
            for (int i = 1; i <= md.getColumnCount(); i++) cols.add(md.getColumnLabel(i).toLowerCase());
            return cols;
        });
    }
}
