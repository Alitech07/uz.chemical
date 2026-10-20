package bilkom.uz.chemical.service.purchases;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.purchases.PurchaseDto;
import bilkom.uz.chemical.entity.purchases.Purchase;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import bilkom.uz.chemical.repository.products.ProductRepository;
import bilkom.uz.chemical.repository.purchases.PurchaseRepository;
import bilkom.uz.chemical.repository.suppliers.SupplierRepository;
import bilkom.uz.chemical.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
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

    public Result add(PurchaseDto dto) {
        var supplierOpt = supplierRepository.findById(dto.getSupplierId());
        if (supplierOpt.isEmpty()) return new Result("Yetkazib beruvchi topilmadi", false);
        var productOpt = productRepository.findById(dto.getProductId());
        if (productOpt.isEmpty()) return new Result("Mahsulot topilmadi", false);

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplierOpt.get());
        purchase.setProduct(productOpt.get());
        purchase.setCountry(dto.getCountry());
        purchase.setBrand(dto.getBrand());
        purchase.setDeliveryType(dto.getDeliveryType());
        purchase.setPaymentType(dto.getPaymentType());
        purchase.setCertificatePath(dto.getCertificatePath());
        purchase.setPurchaseDescription(dto.getPurchaseDescription());
        purchase.setPrice(dto.getPrice());
        purchase.setAmount(dto.getAmount());
        purchase.setCostPrice(dto.getCostPrice());
        purchase.setTransportExpenses(dto.getTransportExpenses());
        purchase.setCustomsExpenses(dto.getCustomsExpenses());
        purchase.setStatus(dto.getStatus() != null ? dto.getStatus() : PurchaseStatus.PENDING);
        purchase.setState(dto.getState() != null ? dto.getState() : bilkom.uz.chemical.entity.purchases.PurchaseState.ACTIVE);
        securityUtils.getCurrentUser().ifPresent(purchase::setCreatedBy);
        purchaseRepository.save(purchase);
        return new Result("Xarid qo'shildi", true);
    }

    public Result edit(Long id, PurchaseDto dto) {
        return purchaseRepository.findById(id).map(purchase -> {
            supplierRepository.findById(dto.getSupplierId())
                    .ifPresent(purchase::setSupplier);
            productRepository.findById(dto.getProductId())
                    .ifPresent(purchase::setProduct);
            purchase.setCountry(dto.getCountry());
            purchase.setBrand(dto.getBrand());
            purchase.setDeliveryType(dto.getDeliveryType());
            purchase.setPaymentType(dto.getPaymentType());
            purchase.setCertificatePath(dto.getCertificatePath());
            purchase.setPurchaseDescription(dto.getPurchaseDescription());
            purchase.setPrice(dto.getPrice());
            if (dto.getAmount() != null) purchase.setAmount(dto.getAmount());
            if (dto.getCostPrice() != null) purchase.setCostPrice(dto.getCostPrice());
            if (dto.getTransportExpenses() != null) purchase.setTransportExpenses(dto.getTransportExpenses());
            if (dto.getCustomsExpenses() != null) purchase.setCustomsExpenses(dto.getCustomsExpenses());
            if (dto.getStatus() != null) purchase.setStatus(dto.getStatus());
            if (dto.getState() != null) purchase.setState(dto.getState());
            purchaseRepository.save(purchase);
            return new Result("Xarid tahrirlandi", true);
        }).orElse(new Result("Xarid topilmadi", false));
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
