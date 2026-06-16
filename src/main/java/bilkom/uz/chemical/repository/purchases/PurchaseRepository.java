package bilkom.uz.chemical.repository.purchases;

import bilkom.uz.chemical.entity.purchases.Purchase;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllBySupplierId(Long supplierId);
    List<Purchase> findAllByStatus(PurchaseStatus status);
}
