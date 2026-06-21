package bilkom.uz.chemical.service.employees;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.employees.EmployeeDto;
import bilkom.uz.chemical.entity.employees.Employee;
import bilkom.uz.chemical.repository.employees.EmployeeRepository;
import bilkom.uz.chemical.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final SecurityUtils securityUtils;

    public Result getAll() {
        return new Result("OK", true, employeeRepository.findAll());
    }

    public Result getById(Long id) {
        return employeeRepository.findById(id)
                .map(e -> new Result("OK", true, e))
                .orElse(new Result("Xodim topilmadi", false));
    }

    public Result add(EmployeeDto dto) {
        Employee employee = new Employee();
        mapDtoToEntity(dto, employee);
        securityUtils.getCurrentUser().ifPresent(employee::setCreatedBy);
        employeeRepository.save(employee);
        return new Result("Xodim qo'shildi", true);
    }

    public Result edit(Long id, EmployeeDto dto) {
        return employeeRepository.findById(id).map(employee -> {
            mapDtoToEntity(dto, employee);
            employeeRepository.save(employee);
            return new Result("Xodim tahrirlandi", true);
        }).orElse(new Result("Xodim topilmadi", false));
    }

    public Result delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            return new Result("Xodim topilmadi", false);
        }
        employeeRepository.deleteById(id);
        return new Result("Xodim o'chirildi", true);
    }

    private void mapDtoToEntity(EmployeeDto dto, Employee employee) {
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setMiddleName(dto.getMiddleName());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setEmail(dto.getEmail());
        employee.setAddress(dto.getAddress());
        employee.setInps(dto.getInps());
        employee.setPasportCode(dto.getPasportCode());
        employee.setPhoto(dto.getPhoto());
        if (dto.getState() != null) employee.setState(dto.getState());
    }
}
