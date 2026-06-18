package bilkom.uz.chemical.controller.admin;

import bilkom.uz.chemical.dto.PermissionDto;
import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.service.admin.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(permissionService.getAll());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.delete(id));
    }
}
