package bilkom.uz.chemical.service.settings;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.settings.SettingsItemDto;
import bilkom.uz.chemical.entity.settings.Currency;
import bilkom.uz.chemical.entity.settings.DeliveryType;
import bilkom.uz.chemical.entity.settings.PaymentType;
import bilkom.uz.chemical.repository.settings.CurrencyRepository;
import bilkom.uz.chemical.repository.settings.DeliveryTypeRepository;
import bilkom.uz.chemical.repository.settings.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final PaymentTypeRepository paymentTypeRepo;
    private final DeliveryTypeRepository deliveryTypeRepo;
    private final CurrencyRepository currencyRepo;

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

    public Result getAllDeliveryTypes() {
        return new Result("OK", true, deliveryTypeRepo.findAll());
    }

    public Result addDeliveryType(SettingsItemDto dto) {
        DeliveryType entity = new DeliveryType();
        entity.setName(dto.getName());
        deliveryTypeRepo.save(entity);
        return new Result("Yetkazib berish turi qo'shildi", true);
    }

    public Result editDeliveryType(Long id, SettingsItemDto dto) {
        return deliveryTypeRepo.findById(id).map(entity -> {
            entity.setName(dto.getName());
            deliveryTypeRepo.save(entity);
            return new Result("Yetkazib berish turi tahrirlandi", true);
        }).orElse(new Result("Topilmadi", false));
    }

    public Result deleteDeliveryType(Long id) {
        if (!deliveryTypeRepo.existsById(id)) return new Result("Topilmadi", false);
        deliveryTypeRepo.deleteById(id);
        return new Result("O'chirildi", true);
    }

    public Result getAllCurrencies() {
        return new Result("OK", true, currencyRepo.findAll());
    }

    public Result addCurrency(SettingsItemDto dto) {
        Currency entity = new Currency();
        entity.setName(dto.getName());
        currencyRepo.save(entity);
        return new Result("Valyuta qo'shildi", true);
    }

    public Result editCurrency(Long id, SettingsItemDto dto) {
        return currencyRepo.findById(id).map(entity -> {
            entity.setName(dto.getName());
            currencyRepo.save(entity);
            return new Result("Valyuta tahrirlandi", true);
        }).orElse(new Result("Topilmadi", false));
    }

    public Result deleteCurrency(Long id) {
        if (!currencyRepo.existsById(id)) return new Result("Topilmadi", false);
        currencyRepo.deleteById(id);
        return new Result("O'chirildi", true);
    }
}
