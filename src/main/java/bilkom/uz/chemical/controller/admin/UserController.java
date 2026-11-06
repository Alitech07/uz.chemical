package bilkom.uz.chemical.controller.admin;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.UserDto;
import bilkom.uz.chemical.security.SecurityUtils;
import bilkom.uz.chemical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
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

    @GetMapping("/me")
    public ResponseEntity<Result> getMe() {
        return securityUtils.getCurrentUser()
                .map(user -> ResponseEntity.ok(userService.getById(user.getId())))
                .orElse(ResponseEntity.status(401).body(new Result("Foydalanuvchi topilmadi", false, null)));
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

    @PostMapping("/upload-avatar")
    public ResponseEntity<Result> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return securityUtils.getCurrentUser()
                .map(user -> ResponseEntity.ok(userService.uploadAvatar(user.getId(), file)))
                .orElse(ResponseEntity.status(401).body(new Result("Foydalanuvchi topilmadi", false, null)));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Result> updateProfile(@RequestBody Map<String, String> body) {
        return securityUtils.getCurrentUser()
                .map(user -> ResponseEntity.ok(userService.updateProfile(
                        user.getId(),
                        body.get("fullname"),
                        body.get("oldPassword"),
                        body.get("newPassword")
                )))
                .orElse(ResponseEntity.status(401).body(new Result("Foydalanuvchi topilmadi", false, null)));
    }

    @GetMapping("/avatar/{fileName}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String fileName) {
        try {
            Path filePath = Path.of("uploads/avatars").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) return ResponseEntity.notFound().build();
            String contentType = "image/jpeg";
            if (fileName.endsWith(".png")) contentType = "image/png";
            else if (fileName.endsWith(".gif")) contentType = "image/gif";
            else if (fileName.endsWith(".webp")) contentType = "image/webp";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/my-permissions")
    public ResponseEntity<Result> getMyPermissions() {
        Set<String> codes = securityUtils.getCurrentUser()
                .map(user -> user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(p -> p.getCode())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
        return ResponseEntity.ok(new Result("OK", true, codes));
    }
}
