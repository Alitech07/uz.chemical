package bilkom.uz.chemical.controller.orders;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.orders.OrderDto;
import bilkom.uz.chemical.entity.orders.OrderStatus;
import bilkom.uz.chemical.service.orders.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<Result> getByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.getByClient(clientId));
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<Result> getByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.getByStatus(status));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody OrderDto dto) {
        return ResponseEntity.ok(orderService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody OrderDto dto) {
        return ResponseEntity.ok(orderService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.delete(id));
    }
}
