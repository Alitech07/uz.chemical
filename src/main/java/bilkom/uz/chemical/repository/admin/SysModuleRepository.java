package bilkom.uz.chemical.repository.admin;

import bilkom.uz.chemical.entity.admin.SysModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysModuleRepository extends JpaRepository<SysModule, Long> {
}
