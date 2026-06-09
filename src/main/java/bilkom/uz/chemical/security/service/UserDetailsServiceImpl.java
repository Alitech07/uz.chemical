package bilkom.uz.chemical.security.service;


import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Foydalanuvchi topilmadi: " + username)
                );

        System.out.println("userdetailser "+username+" "+user.getUsername());

        // Barcha rollar va permissionlarni yig'ish
        Set<GrantedAuthority> authorities = new HashSet<>();

        user.getRoles().forEach(role -> {

            // Rol qo'shish
            authorities.add(
                    new SimpleGrantedAuthority("ROLE_" + role.getName())
            );

            // Rol ichidagi permissionlar
//            role.getPermissions().forEach(permission ->
//                    authorities.add(
//                            new SimpleGrantedAuthority(
//                                    permission.getHttpMethod() + ":" + permission.getUrlPattern()
//                            )
//                    )
//            );
        });

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(!user.isActive())
                .build();
    }
}
