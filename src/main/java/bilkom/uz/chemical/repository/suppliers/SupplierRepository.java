package bilkom.uz.chemical.repository.suppliers;

import bilkom.uz.chemical.entity.suppliers.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findAllByIsActiveTrue();
}
