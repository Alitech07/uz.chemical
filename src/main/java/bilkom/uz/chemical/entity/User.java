package bilkom.uz.chemical.entity;

import bilkom.uz.chemical.entity.admin.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String login;
    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private String password;
    private String isActive="A";

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name= "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Roles> roles = new HashSet<>();

}
