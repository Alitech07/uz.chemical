package bilkom.uz.chemical.controller.suppliers;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.suppliers.SupplierDto;
import bilkom.uz.chemical.service.suppliers.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(supplierService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody SupplierDto dto) {
        return ResponseEntity.ok(supplierService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody SupplierDto dto) {
        return ResponseEntity.ok(supplierService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.delete(id));
    }
}
