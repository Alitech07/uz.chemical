package bilkom.uz.chemical.controller.employees;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.employees.EmployeeDto;
import bilkom.uz.chemical.service.employees.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.delete(id));
    }
}
