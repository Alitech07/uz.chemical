package bilkom.uz.chemical.service.admin;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.admin.SysModuleDto;
import bilkom.uz.chemical.entity.admin.Section;
import bilkom.uz.chemical.entity.admin.SysModule;
import bilkom.uz.chemical.repository.admin.SectionRepository;
import bilkom.uz.chemical.repository.admin.SysModuleRepository;
import bilkom.uz.chemical.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysModuleService {

    private final SysModuleRepository sysModuleRepository;
    private final SectionRepository sectionRepository;
    private final SecurityUtils securityUtils;

    public Result getAll() {
        return new Result("OK", true, sysModuleRepository.findAll());
    }

    public Result getById(Long id) {
        return sysModuleRepository.findById(id)
                .map(m -> new Result("OK", true, m))
                .orElse(new Result("Modul topilmadi", false));
    }

    public Result add(SysModuleDto dto) {
        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("Section topilmadi: " + dto.getSectionId()));
        SysModule module = new SysModule();
        mapDto(dto, module, section);
        sysModuleRepository.save(module);
        return new Result("Modul qo'shildi", true);
    }

    public Result edit(Long id, SysModuleDto dto) {
        return sysModuleRepository.findById(id).map(module -> {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Section topilmadi: " + dto.getSectionId()));
            mapDto(dto, module, section);
            sysModuleRepository.save(module);
            return new Result("Modul yangilandi", true);
        }).orElse(new Result("Modul topilmadi", false));
    }

    public Result delete(Long id) {
        if (!sysModuleRepository.existsById(id)) {
            return new Result("Modul topilmadi", false);
        }
        sysModuleRepository.deleteById(id);
        return new Result("Modul o'chirildi", true);
    }

    public Result getMyModules() {
        List<SysModule> modules = securityUtils.getCurrentUser()
                .map(user -> user.getRoles().stream()
                        .flatMap(role -> role.getModules().stream())
                        .filter(m -> "ACTIVE".equals(m.getState()))
                        .distinct()
                        .sorted(Comparator
                                .comparingInt((SysModule m) ->
                                        m.getSection() != null && m.getSection().getOrderNum() != null
                                                ? m.getSection().getOrderNum() : 99)
                                .thenComparingInt(m -> m.getOrderNum() != null ? m.getOrderNum() : 0))
                        .collect(Collectors.toList()))
                .orElse(List.of());
        return new Result("OK", true, modules);
    }

    private void mapDto(SysModuleDto dto, SysModule module, Section section) {
        module.setName(dto.getName());
        module.setLabel(dto.getLabel());
        module.setRoute(dto.getRoute());
        module.setSection(section);
        module.setOrderNum(dto.getOrderNum() != null ? dto.getOrderNum() : 0);
        if (dto.getState() != null) module.setState(dto.getState());
    }
}
