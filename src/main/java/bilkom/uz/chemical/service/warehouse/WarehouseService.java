package bilkom.uz.chemical.service.warehouse;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.warehouse.WarehouseDto;
import bilkom.uz.chemical.entity.warehouse.Warehouse;
import bilkom.uz.chemical.entity.warehouse.WarehouseState;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.repository.products.ProductRepository;
import bilkom.uz.chemical.repository.warehouse.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

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
        return productRepository.findById(dto.getProductId()).map(product -> {
            Warehouse warehouse = new Warehouse();
            warehouse.setProduct(product);
            warehouse.setResidual(dto.getResidual());
            warehouse.setMeasure(dto.getMeasure());
            warehouse.setStorageSpace(dto.getStorageSpace());
            warehouse.setState(dto.getState() != null ? dto.getState() : WarehouseState.ACTIVE);
            if (dto.getCreatedById() != null) {
                userRepository.findById(dto.getCreatedById())
                        .ifPresent(warehouse::setCreatedBy);
            }
            warehouseRepository.save(warehouse);
            return new Result("Ombor yozuvi qo'shildi", true);
        }).orElse(new Result("Mahsulot topilmadi", false));
    }

    public Result edit(Long id, WarehouseDto dto) {
        return warehouseRepository.findById(id).map(warehouse -> {
            productRepository.findById(dto.getProductId())
                    .ifPresent(warehouse::setProduct);
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
}
