package bilkom.uz.chemical.controller.products;

import bilkom.uz.chemical.dto.products.CategoryDto;
import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.service.products.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody CategoryDto dto) {
        return ResponseEntity.ok(categoryService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
