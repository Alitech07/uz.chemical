package bilkom.uz.chemical.controller;


import bilkom.uz.chemical.dto.LoginRequestDto;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        System.out.println("shu yerga keldi");
        System.out.println(request.getUsername());
        try {
            // 1. Autentifikatsiya
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // 2. Token yaratish
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtils.generateToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities()
            );

            return ResponseEntity.ok(Map.of(
                    "accessToken", token,
                    "username", userDetails.getUsername()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Username yoki parol noto'g'ri!"));
        }
    }

}
