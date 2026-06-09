package bilkom.uz.chemical.controller.admin;

import bilkom.uz.chemical.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/list")
    public ResponseEntity<User> getUsers(){
        return ResponseEntity.status(200).body(new User());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(){
        return ResponseEntity.status(400).body("User kiritishda xatolik bo'ldi");
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUser(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Parol yoki login xato");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(){
        return ResponseEntity.ok().body("Xodim muvafaqiyatli o'chirildi.");
    }
}
