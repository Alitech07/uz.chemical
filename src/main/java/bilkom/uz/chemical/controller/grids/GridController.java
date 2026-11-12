package bilkom.uz.chemical.controller.grids;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.service.GridService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/grids")
public class GridController {

    private final GridService gridService;

    public GridController(GridService gridService) {
        this.gridService = gridService;
    }

    // Any query param other than "page" is treated as a column filter (e.g. ?purchase_id=5)
    @GetMapping("/{gridId}")
    public ResponseEntity<Result> getGrid(
        @PathVariable Long gridId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam Map<String, String> params
    ) {
        Map<String, String> filters = new LinkedHashMap<>(params);
        filters.remove("page");
        return ResponseEntity.ok(gridService.getByGridId(gridId, page, filters));
    }
}
