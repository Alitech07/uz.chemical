package bilkom.uz.chemical.config;

import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            if (userRepository.findByLogin("admin").isEmpty()) {
                User admin = new User();
                admin.setLogin("admin");
                admin.setFullname("Admin User");
                admin.setPassword(passwordEncoder.encode("Admin1234"));
                admin.setActive(true);
                userRepository.save(admin);
                System.out.println("=== Default admin user created: login=admin, password=Admin1234 ===");
            } else {
                System.out.println("=== Admin user already exists ===");
            }
        } catch (Exception e) {
            System.err.println("=== DataInitializer ERROR: " + e.getMessage() + " ===");
            e.printStackTrace();
        }
    }
}
