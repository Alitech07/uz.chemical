package bilkom.uz.chemical.service;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.UserDto;
import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.entity.admin.Roles;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.repository.admin.RolesRepository;
import bilkom.uz.chemical.repository.orders.OrderRepository;
import bilkom.uz.chemical.repository.purchases.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderRepository orderRepository;
    private final PurchaseRepository purchaseRepository;

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

    public Result updateProfile(Long id, String fullname, String oldPassword, String newPassword,
                                String position, String phone, String address) {
        return userRepository.findById(id).map(user -> {
            if (fullname != null && !fullname.isBlank()) user.setFullname(fullname);
            if (position != null) user.setPosition(position);
            if (phone != null) user.setPhone(phone);
            if (address != null) user.setAddress(address);
            if (newPassword != null && !newPassword.isBlank()) {
                if (oldPassword == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
                    return new Result("Joriy parol noto'g'ri", false);
                }
                user.setPassword(passwordEncoder.encode(newPassword));
            }
            userRepository.save(user);
            return new Result("Profil yangilandi", true);
        }).orElse(new Result("Foydalanuvchi topilmadi", false));
    }

    public Result getMyStats(Long userId) {
        LocalDate today = LocalDate.now();
        int thisMonth = today.getMonthValue();
        int thisYear = today.getYear();
        LocalDate prev = today.minusMonths(1);
        int lastMonth = prev.getMonthValue();
        int lastYear = prev.getYear();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orderRepository.countByCreatedById(userId));
        BigDecimal totalAmt = orderRepository.sumAmountByCreatedById(userId);
        stats.put("totalOrdersAmount", totalAmt != null ? totalAmt : BigDecimal.ZERO);
        stats.put("thisMonthOrders", orderRepository.countByCreatedByIdAndMonth(userId, thisMonth, thisYear));
        BigDecimal thisAmt = orderRepository.sumAmountByCreatedByIdAndMonth(userId, thisMonth, thisYear);
        stats.put("thisMonthOrdersAmount", thisAmt != null ? thisAmt : BigDecimal.ZERO);
        stats.put("lastMonthOrders", orderRepository.countByCreatedByIdAndMonth(userId, lastMonth, lastYear));
        BigDecimal lastAmt = orderRepository.sumAmountByCreatedByIdAndMonth(userId, lastMonth, lastYear);
        stats.put("lastMonthOrdersAmount", lastAmt != null ? lastAmt : BigDecimal.ZERO);
        stats.put("totalPurchases", purchaseRepository.countByCreatedById(userId));
        return new Result("OK", true, stats);
    }

    public Result uploadAvatar(Long id, MultipartFile file) {
        return userRepository.findById(id).map(user -> {
            try {
                Path uploadPath = Path.of("uploads/avatars");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                String fileName = id + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
                user.setAvatarPath("uploads/avatars/" + fileName);
                userRepository.save(user);
                return new Result("Avatar yuklandi", true, user.getAvatarPath());
            } catch (Exception e) {
                return new Result("Avatar yuklashda xato: " + e.getMessage(), false, null);
            }
        }).orElse(new Result("Foydalanuvchi topilmadi", false));
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
