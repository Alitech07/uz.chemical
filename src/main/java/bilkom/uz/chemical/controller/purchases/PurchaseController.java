package bilkom.uz.chemical.controller.purchases;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.purchases.PurchaseDto;
import bilkom.uz.chemical.entity.purchases.PurchaseStatus;
import bilkom.uz.chemical.service.purchases.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(purchaseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getById(id));
    }

    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<Result> getBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(purchaseService.getBySupplier(supplierId));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<Result> getByStatus(@PathVariable PurchaseStatus status) {
        return ResponseEntity.ok(purchaseService.getByStatus(status));
    }

    @PostMapping("/upload-certificate")
    public ResponseEntity<Result> uploadCertificate(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(purchaseService.uploadCertificate(file));
    }

    @GetMapping("/certificate/{fileName}")
    public ResponseEntity<Resource> getCertificate(@PathVariable String fileName) {
        try {
            Path filePath = Path.of("uploads/certificates").resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) return ResponseEntity.notFound().build();
            String contentType = fileName.endsWith(".pdf") ? "application/pdf" : "image/jpeg";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody PurchaseDto dto) {
        return ResponseEntity.ok(purchaseService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody PurchaseDto dto) {
        return ResponseEntity.ok(purchaseService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.delete(id));
    }
}
