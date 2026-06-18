package bilkom.uz.chemical.service.suppliers;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.suppliers.SupplierDto;
import bilkom.uz.chemical.entity.suppliers.Supplier;
import bilkom.uz.chemical.repository.suppliers.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public Result getAll() {
        return new Result("OK", true, supplierRepository.findAll());
    }

    public Result getById(Long id) {
        return supplierRepository.findById(id)
                .map(s -> new Result("OK", true, s))
                .orElse(new Result("Yetkazib beruvchi topilmadi", false));
    }

    public Result add(SupplierDto dto) {
        Supplier supplier = new Supplier();
        supplier.setFreightForwarder(dto.getFreightForwarder());
        supplier.setLegalName(dto.getLegalName());
        supplier.setBrandName(dto.getBrandName());
        supplier.setTypeOfActivity(dto.getTypeOfActivity());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setPhone(dto.getPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setActive(dto.isActive());
        supplierRepository.save(supplier);
        return new Result("Yetkazib beruvchi qo'shildi", true);
    }

    public Result edit(Long id, SupplierDto dto) {
        return supplierRepository.findById(id).map(supplier -> {
            supplier.setFreightForwarder(dto.getFreightForwarder());
            supplier.setLegalName(dto.getLegalName());
            supplier.setBrandName(dto.getBrandName());
            supplier.setTypeOfActivity(dto.getTypeOfActivity());
            supplier.setContactPerson(dto.getContactPerson());
            supplier.setPhone(dto.getPhone());
            supplier.setEmail(dto.getEmail());
            supplier.setAddress(dto.getAddress());
            supplier.setActive(dto.isActive());
            supplierRepository.save(supplier);
            return new Result("Yetkazib beruvchi tahrirlandi", true);
        }).orElse(new Result("Yetkazib beruvchi topilmadi", false));
    }

    public Result delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            return new Result("Yetkazib beruvchi topilmadi", false);
        }
        supplierRepository.deleteById(id);
        return new Result("Yetkazib beruvchi o'chirildi", true);
    }
}
