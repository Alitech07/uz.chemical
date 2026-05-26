package bilkom.uz.chemical.repository.admin;

import bilkom.uz.chemical.entity.admin.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
