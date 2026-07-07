package bilkom.uz.chemical.service.warehouse;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.warehouse.WarehouseDto;
import bilkom.uz.chemical.entity.warehouse.Warehouse;
import bilkom.uz.chemical.entity.warehouse.WarehouseState;
import bilkom.uz.chemical.repository.products.ProductRepository;
import bilkom.uz.chemical.repository.purchases.PurchaseRepository;
import bilkom.uz.chemical.repository.warehouse.WarehouseRepository;
import bilkom.uz.chemical.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;
    private final SecurityUtils securityUtils;

    public Result getAll() {
        return new Result("OK", true, warehouseRepository.findAll());
    }

    public Result getById(Long id) {
        return warehouseRepository.findById(id)
                .map(w -> new Result("OK", true, w))
                .orElse(new Result("Ombor yozuvi topilmadi", false));
    }

    public Result getByProduct(Long productId) {
        return warehouseRepository.findByProductId(productId)
                .map(w -> new Result("OK", true, w))
                .orElse(new Result("Bu mahsulot uchun ombor yozuvi topilmadi", false));
    }

    public Result getByState(WarehouseState state) {
        return new Result("OK", true, warehouseRepository.findAllByState(state));
    }

    public Result add(WarehouseDto dto) {
        Warehouse warehouse = new Warehouse();
        warehouse.setResidual(dto.getResidual());
        warehouse.setMeasure(dto.getMeasure());
        warehouse.setStorageSpace(dto.getStorageSpace());
        warehouse.setState(dto.getState() != null ? dto.getState() : WarehouseState.ACTIVE);
        securityUtils.getCurrentUser().ifPresent(warehouse::setCreatedBy);

        if (dto.getPurchaseId() != null) {
            return purchaseRepository.findById(dto.getPurchaseId()).map(purchase -> {
                warehouse.setPurchase(purchase);
                warehouseRepository.save(warehouse);
                return new Result("Ombor yozuvi qo'shildi", true);
            }).orElse(new Result("Xarid topilmadi", false));
        }

        if (dto.getProductId() != null) {
            return productRepository.findById(dto.getProductId()).map(product -> {
                warehouse.setProduct(product);
                warehouseRepository.save(warehouse);
                return new Result("Ombor yozuvi qo'shildi", true);
            }).orElse(new Result("Mahsulot topilmadi", false));
        }

        return new Result("Mahsulot yoki xarid ko'rsatilmagan", false);
    }

    public Result edit(Long id, WarehouseDto dto) {
        return warehouseRepository.findById(id).map(warehouse -> {
            if (dto.getPurchaseId() != null) {
                purchaseRepository.findById(dto.getPurchaseId()).ifPresent(warehouse::setPurchase);
                warehouse.setProduct(null);
            } else if (dto.getProductId() != null) {
                productRepository.findById(dto.getProductId()).ifPresent(warehouse::setProduct);
                warehouse.setPurchase(null);
            }
            warehouse.setResidual(dto.getResidual());
            warehouse.setMeasure(dto.getMeasure());
            warehouse.setStorageSpace(dto.getStorageSpace());
            if (dto.getState() != null) warehouse.setState(dto.getState());
            warehouseRepository.save(warehouse);
            return new Result("Ombor yozuvi tahrirlandi", true);
        }).orElse(new Result("Ombor yozuvi topilmadi", false));
    }

    public Result delete(Long id) {
        if (!warehouseRepository.existsById(id)) {
            return new Result("Ombor yozuvi topilmadi", false);
        }
        warehouseRepository.deleteById(id);
        return new Result("Ombor yozuvi o'chirildi", true);
    }

    public Result getFreeProducts() {
        Set<Long> trackedIds = warehouseRepository.findAll().stream()
                .filter(w -> w.getProduct() != null)
                .map(w -> w.getProduct().getId())
                .collect(Collectors.toSet());
        var freeProducts = productRepository.findAll().stream()
                .filter(p -> !trackedIds.contains(p.getId()))
                .collect(Collectors.toList());
        return new Result("OK", true, freeProducts);
    }
}
