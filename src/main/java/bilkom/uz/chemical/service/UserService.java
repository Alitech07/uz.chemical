package bilkom.uz.chemical.service;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.UserDto;
import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.entity.admin.Roles;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.repository.admin.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public Result getAll() {
        return new Result("OK", true, userRepository.findAll());
    }

    public Result getById(Long id) {
        return userRepository.findById(id)
                .map(user -> new Result("OK", true, user))
                .orElse(new Result("Foydalanuvchi topilmadi", false));
    }

    public Result addUser(UserDto dto) {
        User user = new User();
        user.setFullname(dto.getFullName());
        user.setLogin(dto.getLogin());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : "A");
        user.setRoles(resolveRoles(dto.getRoleIds()));
        userRepository.save(user);
        return new Result("Foydalanuvchi qo'shildi", true);
    }

    public Result editUser(Long id, UserDto dto) {
        return userRepository.findById(id).map(user -> {
            user.setFullname(dto.getFullName());
            user.setLogin(dto.getLogin());
            if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            user.setRoles(resolveRoles(dto.getRoleIds()));
            userRepository.save(user);
            return new Result("Foydalanuvchi tahrirlandi", true);
        }).orElse(new Result("Foydalanuvchi topilmadi", false));
    }

    public Result deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return new Result("Foydalanuvchi topilmadi", false);
        }
        userRepository.deleteById(id);
        return new Result("Foydalanuvchi o'chirildi", true);
    }

    public Result getAllUsers() {
        return getAll();
    }

    private Set<Roles> resolveRoles(Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(rolesRepository.findAllById(roleIds));
    }
}
