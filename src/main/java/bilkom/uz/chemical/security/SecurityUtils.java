package bilkom.uz.chemical.security;

import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByLogin(login);
    }
}
