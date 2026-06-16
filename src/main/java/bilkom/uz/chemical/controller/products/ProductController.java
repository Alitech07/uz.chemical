package bilkom.uz.chemical.controller.products;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.products.ProductDto;
import bilkom.uz.chemical.entity.products.ProductState;
import bilkom.uz.chemical.service.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Result> search(@RequestParam String name) {
        return ResponseEntity.ok(productService.search(name));
    }

    @GetMapping("/by-state/{state}")
    public ResponseEntity<Result> getByState(@PathVariable ProductState state) {
        return ResponseEntity.ok(productService.getByState(state));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody ProductDto dto) {
        return ResponseEntity.ok(productService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(productService.delete(id));
    }
}
