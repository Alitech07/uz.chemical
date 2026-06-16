package bilkom.uz.chemical.repository.admin;

import bilkom.uz.chemical.entity.admin.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(String name);
    boolean existsByName(String name);
}
