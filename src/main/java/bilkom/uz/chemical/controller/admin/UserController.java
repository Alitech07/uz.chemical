package bilkom.uz.chemical.controller.admin;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.UserDto;
import bilkom.uz.chemical.security.SecurityUtils;
import bilkom.uz.chemical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.addUser(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.editUser(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @GetMapping("/my-modules")
    public ResponseEntity<Result> getMyModules() {
        Set<String> modules = securityUtils.getCurrentUser()
                .map(user -> user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(p -> p.getModule())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
        return ResponseEntity.ok(new Result("OK", true, modules));
    }
}
