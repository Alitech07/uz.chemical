package bilkom.uz.chemical.service.admin;

import bilkom.uz.chemical.dto.PermissionDto;
import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.entity.admin.Permission;
import bilkom.uz.chemical.repository.admin.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Result getAll() {
        return new Result("OK", true, permissionRepository.findAll());
    }


    public Result delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            return new Result("Permission topilmadi", false);
        }
        permissionRepository.deleteById(id);
        return new Result("Permission o'chirildi", true);
    }
}
