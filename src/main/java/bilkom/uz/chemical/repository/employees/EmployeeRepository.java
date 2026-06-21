package bilkom.uz.chemical.repository.employees;

import bilkom.uz.chemical.entity.employees.Employee;
import bilkom.uz.chemical.entity.employees.EmployeeState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByState(EmployeeState state);
}
