package bilkom.uz.chemical.controller.purchases;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.purchases.PurchaseDto;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import bilkom.uz.chemical.service.purchases.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(purchaseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getById(id));
    }

    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<Result> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(purchaseService.getBySupplier(supplierId));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<Result> getByStatus(@PathVariable PurchaseStatus status) {
        return ResponseEntity.ok(purchaseService.getByStatus(status));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody PurchaseDto dto) {
        return ResponseEntity.ok(purchaseService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody PurchaseDto dto) {
        return ResponseEntity.ok(purchaseService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.delete(id));
    }
}
