package bilkom.uz.chemical.controller.admin;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.UserDto;
import bilkom.uz.chemical.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<Result> getUserList() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<Result> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser() {
        return ResponseEntity.ok().body("Xodim muvafaqiyatli tahrirlandi.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        return ResponseEntity.ok().body("Xodim muvafaqiyatli o'chirildi.");
    }
}
