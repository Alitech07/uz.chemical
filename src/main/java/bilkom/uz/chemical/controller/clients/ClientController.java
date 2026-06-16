package bilkom.uz.chemical.controller.clients;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.clients.ClientDto;
import bilkom.uz.chemical.entity.clients.ClientState;
import bilkom.uz.chemical.service.clients.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/list")
    public ResponseEntity<Result> getAll() {
        return ResponseEntity.ok(clientService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @GetMapping("/by-inn/{inn}")
    public ResponseEntity<Result> getByInn(@PathVariable String inn) {
        return ResponseEntity.ok(clientService.getByInn(inn));
    }

    @GetMapping("/by-state/{state}")
    public ResponseEntity<Result> getByState(@PathVariable ClientState state) {
        return ResponseEntity.ok(clientService.getByState(state));
    }

    @GetMapping("/by-region/{region}")
    public ResponseEntity<Result> getByRegion(@PathVariable String region) {
        return ResponseEntity.ok(clientService.getByRegion(region));
    }

    @GetMapping("/by-employee/{empId}")
    public ResponseEntity<Result> getByEmployee(@PathVariable Long empId) {
        return ResponseEntity.ok(clientService.getByEmployee(empId));
    }

    @GetMapping("/search")
    public ResponseEntity<Result> search(@RequestParam String name) {
        return ResponseEntity.ok(clientService.search(name));
    }

    @PostMapping("/add")
    public ResponseEntity<Result> add(@RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.add(dto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.edit(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Result> delete(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.delete(id));
    }
}
