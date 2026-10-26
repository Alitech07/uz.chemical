package bilkom.uz.chemical.service.purchases;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.purchases.PurchaseDto;
import bilkom.uz.chemical.dto.purchases.PurchaseItemDto;
import bilkom.uz.chemical.entity.purchases.Purchase;
import bilkom.uz.chemical.entity.purchases.PurchaseItem;
import bilkom.uz.chemical.entity.purchases.PurchaseState;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import bilkom.uz.chemical.repository.products.ProductRepository;
import bilkom.uz.chemical.repository.purchases.PurchaseRepository;
import bilkom.uz.chemical.repository.settings.CurrencyRepository;
import bilkom.uz.chemical.repository.settings.DeliveryTypeRepository;
import bilkom.uz.chemical.repository.settings.PaymentTypeRepository;
import bilkom.uz.chemical.repository.suppliers.SupplierRepository;
import bilkom.uz.chemical.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final SecurityUtils securityUtils;

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

    @Transactional
    public Result add(PurchaseDto dto) {
        var supplierOpt = supplierRepository.findById(dto.getSupplierId());
        if (supplierOpt.isEmpty()) return new Result("Yetkazib beruvchi topilmadi", false);

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplierOpt.get());
        purchase.setCountry(dto.getCountry());
        purchase.setBrand(dto.getBrand());
        if (dto.getDeliveryTypeId() != null)
            deliveryTypeRepository.findById(dto.getDeliveryTypeId()).ifPresent(purchase::setDeliveryType);
        if (dto.getPaymentTypeId() != null)
            paymentTypeRepository.findById(dto.getPaymentTypeId()).ifPresent(purchase::setPaymentType);
        if (dto.getCurrencyId() != null)
            currencyRepository.findById(dto.getCurrencyId()).ifPresent(purchase::setCurrency);
        purchase.setCertificatePath(dto.getCertificatePath());
        purchase.setPurchaseDescription(dto.getPurchaseDescription());
        purchase.setTransportExpenses(dto.getTransportExpenses());
        purchase.setCustomsExpenses(dto.getCustomsExpenses());
        purchase.setStatus(dto.getStatus() != null ? dto.getStatus() : PurchaseStatus.PENDING);
        purchase.setState(dto.getState() != null ? dto.getState() : PurchaseState.ACTIVE);
        securityUtils.getCurrentUser().ifPresent(purchase::setCreatedBy);

        List<PurchaseItem> items = buildItems(dto.getItems(), purchase);
        purchase.setItems(items);

        purchaseRepository.save(purchase);
        return new Result("Xarid qo'shildi", true);
    }

    @Transactional
    public Result edit(Long id, PurchaseDto dto) {
        return purchaseRepository.findById(id).map(purchase -> {
            if (dto.getSupplierId() != null)
                supplierRepository.findById(dto.getSupplierId()).ifPresent(purchase::setSupplier);
            purchase.setCountry(dto.getCountry());
            purchase.setBrand(dto.getBrand());
            if (dto.getDeliveryTypeId() != null)
                deliveryTypeRepository.findById(dto.getDeliveryTypeId()).ifPresent(purchase::setDeliveryType);
            if (dto.getPaymentTypeId() != null)
                paymentTypeRepository.findById(dto.getPaymentTypeId()).ifPresent(purchase::setPaymentType);
            if (dto.getCurrencyId() != null)
                currencyRepository.findById(dto.getCurrencyId()).ifPresent(purchase::setCurrency);
            purchase.setCertificatePath(dto.getCertificatePath());
            purchase.setPurchaseDescription(dto.getPurchaseDescription());
            if (dto.getTransportExpenses() != null) purchase.setTransportExpenses(dto.getTransportExpenses());
            if (dto.getCustomsExpenses() != null) purchase.setCustomsExpenses(dto.getCustomsExpenses());
            if (dto.getStatus() != null) purchase.setStatus(dto.getStatus());
            if (dto.getState() != null) purchase.setState(dto.getState());

            if (dto.getItems() != null) {
                purchase.getItems().clear();
                purchase.getItems().addAll(buildItems(dto.getItems(), purchase));
            }

            purchaseRepository.save(purchase);
            return new Result("Xarid tahrirlandi", true);
        }).orElse(new Result("Xarid topilmadi", false));
    }

    private List<PurchaseItem> buildItems(List<PurchaseItemDto> dtos, Purchase purchase) {
        List<PurchaseItem> items = new ArrayList<>();
        if (dtos == null) return items;
        for (PurchaseItemDto d : dtos) {
            var productOpt = productRepository.findById(d.getProductId());
            if (productOpt.isEmpty()) continue;
            PurchaseItem item = new PurchaseItem();
            item.setPurchase(purchase);
            item.setProduct(productOpt.get());
            item.setPrice(d.getPrice());
            item.setAmount(d.getAmount());
            items.add(item);
        }
        return items;
    }

    public Result uploadCertificate(MultipartFile file) {
        try {
            Path uploadPath = Path.of("uploads/certificates");
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
            return new Result("Fayl yuklandi", true, "uploads/certificates/" + fileName);
        } catch (Exception e) {
            return new Result("Fayl yuklashda xato: " + e.getMessage(), false, null);
        }
    }

    public Result delete(Long id) {
        if (!purchaseRepository.existsById(id)) {
            return new Result("Xarid topilmadi", false);
        }
        purchaseRepository.deleteById(id);
        return new Result("Xarid o'chirildi", true);
    }
}
