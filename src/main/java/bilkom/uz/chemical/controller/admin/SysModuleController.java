package bilkom.uz.chemical.controller.admin;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.admin.SysModuleDto;
import bilkom.uz.chemical.service.admin.SysModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class SysModuleController {

    private final SysModuleService sysModuleService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(sysModuleService.getAll());
    }

    @GetMapping("/my-modules")
    public ResponseEntity<Result> getMyModules() {
        return ResponseEntity.ok(sysModuleService.getMyModules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sysModuleService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody SysModuleDto dto) {
        return ResponseEntity.ok(sysModuleService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody SysModuleDto dto) {
        return ResponseEntity.ok(sysModuleService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(sysModuleService.delete(id));
    }
}
