package bilkom.uz.chemical.service;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.UserDto;
import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Result addUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getFullName());
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(userDto.isActive());
        userRepository.save(user);
        return new Result("Foydalanuvchi muvaffaqiyatli qo'shildi", true);
    }

    public Result getAllUsers() {
        List<User> users = userRepository.findAll();
        return new Result("OK", true, users);
    }
}
