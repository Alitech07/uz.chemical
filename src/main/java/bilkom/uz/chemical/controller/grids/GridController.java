package bilkom.uz.chemical.controller.grids;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.service.GridService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grids")
public class GridController {

    private final GridService gridService;

    public GridController(GridService gridService) {
        this.gridService = gridService;
    }

    @GetMapping("/{gridId}")
    public ResponseEntity<Result> getGrid(
        @PathVariable Long gridId,
        @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(gridService.getByGridId(gridId, page));
    }
}
