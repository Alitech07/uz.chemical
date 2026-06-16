package bilkom.uz.chemical.repository.warehouse;

import bilkom.uz.chemical.entity.warehouse.Warehouse;
import bilkom.uz.chemical.entity.warehouse.WarehouseState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findAllByState(WarehouseState state);
    Optional<Warehouse> findByProductId(Long productId);
}
