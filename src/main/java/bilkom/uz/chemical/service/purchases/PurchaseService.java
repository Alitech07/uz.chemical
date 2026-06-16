package bilkom.uz.chemical.service.purchases;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.purchases.PurchaseDto;
import bilkom.uz.chemical.entity.purchases.Purchase;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.repository.purchases.PurchaseRepository;
import bilkom.uz.chemical.repository.suppliers.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public Result getAll() {
        return new Result("OK", true, purchaseRepository.findAll());
    }

    public Result getById(Long id) {
        return purchaseRepository.findById(id)
                .map(p -> new Result("OK", true, p))
                .orElse(new Result("Xarid topilmadi", false));
    }

    public Result getBySupplier(Long supplierId) {
        return new Result("OK", true, purchaseRepository.findAllBySupplierId(supplierId));
    }

    public Result getByStatus(PurchaseStatus status) {
        return new Result("OK", true, purchaseRepository.findAllByStatus(status));
    }

    public Result add(PurchaseDto dto) {
        return supplierRepository.findById(dto.getSupplierId()).map(supplier -> {
            Purchase purchase = new Purchase();
            purchase.setSupplier(supplier);
            purchase.setPurchaseDescription(dto.getPurchaseDescription());
            purchase.setPrice(dto.getPrice());
            purchase.setAmount(dto.getAmount());
            purchase.setStatus(dto.getStatus() != null ? dto.getStatus() : PurchaseStatus.PENDING);
            purchase.setState(dto.getState());
            if (dto.getCreatedById() != null) {
                userRepository.findById(dto.getCreatedById())
                        .ifPresent(purchase::setCreatedBy);
            }
            purchaseRepository.save(purchase);
            return new Result("Xarid qo'shildi", true);
        }).orElse(new Result("Yetkazib beruvchi topilmadi", false));
    }

    public Result edit(Long id, PurchaseDto dto) {
        return purchaseRepository.findById(id).map(purchase -> {
            supplierRepository.findById(dto.getSupplierId())
                    .ifPresent(purchase::setSupplier);
            purchase.setPurchaseDescription(dto.getPurchaseDescription());
            purchase.setPrice(dto.getPrice());
            purchase.setAmount(dto.getAmount());
            if (dto.getStatus() != null) purchase.setStatus(dto.getStatus());
            if (dto.getState() != null) purchase.setState(dto.getState());
            purchaseRepository.save(purchase);
            return new Result("Xarid tahrirlandi", true);
        }).orElse(new Result("Xarid topilmadi", false));
    }

    public Result delete(Long id) {
        if (!purchaseRepository.existsById(id)) {
            return new Result("Xarid topilmadi", false);
        }
        purchaseRepository.deleteById(id);
        return new Result("Xarid o'chirildi", true);
    }
}
