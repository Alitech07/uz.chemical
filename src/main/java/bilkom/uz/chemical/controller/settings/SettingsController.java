package bilkom.uz.chemical.controller.settings;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.settings.SettingsItemDto;
import bilkom.uz.chemical.service.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping("/payment-types/list")
    public ResponseEntity<Result> getPaymentTypes() {
        return ResponseEntity.ok(settingsService.getAllPaymentTypes());
    }

    @PostMapping("/payment-types/add")
    public ResponseEntity<Result> addPaymentType(@RequestBody SettingsItemDto dto) {
        return ResponseEntity.ok(settingsService.addPaymentType(dto));
    }

    @PutMapping("/payment-types/edit/{id}")
    public ResponseEntity<Result> editPaymentType(@PathVariable Long id, @RequestBody SettingsItemDto dto) {
        return ResponseEntity.ok(settingsService.editPaymentType(id, dto));
    }

    @DeleteMapping("/payment-types/delete/{id}")
    public ResponseEntity<Result> deletePaymentType(@PathVariable Long id) {
        return ResponseEntity.ok(settingsService.deletePaymentType(id));
    }
}
