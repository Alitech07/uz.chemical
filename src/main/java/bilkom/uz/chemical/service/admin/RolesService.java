package bilkom.uz.chemical.service.admin;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.RolesDto;
import bilkom.uz.chemical.entity.admin.Permission;
import bilkom.uz.chemical.entity.admin.Roles;
import bilkom.uz.chemical.entity.admin.SysModule;
import bilkom.uz.chemical.repository.admin.PermissionRepository;
import bilkom.uz.chemical.repository.admin.RolesRepository;
import bilkom.uz.chemical.repository.admin.SysModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolesService {

    private final RolesRepository rolesRepository;
    private final PermissionRepository permissionRepository;
    private final SysModuleRepository sysModuleRepository;

    public Result getAll() {
        return new Result("OK", true, rolesRepository.findAll());
    }

    public Result getById(Long id) {
        return rolesRepository.findById(id)
                .map(role -> new Result("OK", true, role))
                .orElse(new Result("Rol topilmadi", false));
    }

    public Result add(RolesDto dto) {
        if (rolesRepository.existsByName(dto.getName())) {
            return new Result("Bu nomdagi rol allaqachon mavjud", false);
        }
        Roles role = new Roles();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setPermissions(resolvePermissions(dto.getPermissionIds()));
        role.setModules(resolveModules(dto.getModuleIds()));
        rolesRepository.save(role);
        return new Result("Rol qo'shildi", true);
    }

    public Result edit(Long id, RolesDto dto) {
        return rolesRepository.findById(id).map(role -> {
            role.setName(dto.getName());
            role.setDescription(dto.getDescription());
            if (dto.getPermissionIds() != null) {
                role.setPermissions(resolvePermissions(dto.getPermissionIds()));
            }
            if (dto.getModuleIds() != null) {
                role.setModules(resolveModules(dto.getModuleIds()));
            }
            rolesRepository.save(role);
            return new Result("Rol tahrirlandi", true);
        }).orElse(new Result("Rol topilmadi", false));
    }

    public Result delete(Long id) {
        if (!rolesRepository.existsById(id)) {
            return new Result("Rol topilmadi", false);
        }
        rolesRepository.deleteById(id);
        return new Result("Rol o'chirildi", true);
    }

    private Set<Permission> resolvePermissions(Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(permissionRepository.findAllById(permissionIds));
    }

    private Set<SysModule> resolveModules(Set<Long> moduleIds) {
        if (moduleIds == null || moduleIds.isEmpty()) return new HashSet<>();
        return new HashSet<>(sysModuleRepository.findAllById(moduleIds));
    }
}
