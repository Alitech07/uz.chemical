package bilkom.uz.chemical.service.settings;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.settings.SettingsItemDto;
import bilkom.uz.chemical.entity.settings.PaymentType;
import bilkom.uz.chemical.repository.settings.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final PaymentTypeRepository paymentTypeRepo;

    public Result getAllPaymentTypes() {
        return new Result("OK", true, paymentTypeRepo.findAll());
    }

    public Result addPaymentType(SettingsItemDto dto) {
        PaymentType entity = new PaymentType();
        entity.setName(dto.getName());
        paymentTypeRepo.save(entity);
        return new Result("To'lov turi qo'shildi", true);
    }

    public Result editPaymentType(Long id, SettingsItemDto dto) {
        return paymentTypeRepo.findById(id).map(entity -> {
            entity.setName(dto.getName());
            paymentTypeRepo.save(entity);
            return new Result("To'lov turi tahrirlandi", true);
        }).orElse(new Result("Topilmadi", false));
    }

    public Result deletePaymentType(Long id) {
        if (!paymentTypeRepo.existsById(id)) return new Result("Topilmadi", false);
        paymentTypeRepo.deleteById(id);
        return new Result("O'chirildi", true);
    }
}
