package bilkom.uz.chemical.controller.warehouse;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.warehouse.WarehouseDto;
import bilkom.uz.chemical.entity.warehouse.WarehouseState;
import bilkom.uz.chemical.service.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(warehouseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getById(id));
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<Result> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(warehouseService.getByProduct(productId));
    }

    @GetMapping("/by-state/{state}")
    public ResponseEntity<Result> getByState(@PathVariable WarehouseState state) {
        return ResponseEntity.ok(warehouseService.getByState(state));
    }

    @GetMapping("/free-products")
    public ResponseEntity<Result> getFreeProducts() {
        return ResponseEntity.ok(warehouseService.getFreeProducts());
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody WarehouseDto dto) {
        return ResponseEntity.ok(warehouseService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody WarehouseDto dto) {
        return ResponseEntity.ok(warehouseService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.delete(id));
    }
}
