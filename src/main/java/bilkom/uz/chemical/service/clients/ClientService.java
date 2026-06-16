package bilkom.uz.chemical.service.clients;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.clients.ClientDto;
import bilkom.uz.chemical.entity.clients.Client;
import bilkom.uz.chemical.entity.clients.ClientState;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.repository.clients.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public Result getAll() {
        return new Result("OK", true, clientRepository.findAll());
    }

    public Result getById(Long id) {
        return clientRepository.findById(id)
                .map(c -> new Result("OK", true, c))
                .orElse(new Result("Mijoz topilmadi", false));
    }

    public Result getByInn(String inn) {
        return clientRepository.findByInn(inn)
                .map(c -> new Result("OK", true, c))
                .orElse(new Result("Bu INN bo'yicha mijoz topilmadi", false));
    }

    public Result getByState(ClientState state) {
        return new Result("OK", true, clientRepository.findAllByState(state));
    }

    public Result getByRegion(String region) {
        return new Result("OK", true, clientRepository.findAllByRegion(region));
    }

    public Result getByEmployee(Long empId) {
        return new Result("OK", true, clientRepository.findAllByEmployeeId(empId));
    }

    public Result search(String name) {
        return new Result("OK", true,
                clientRepository.findAllByClientNameContainingIgnoreCase(name));
    }

    public Result add(ClientDto dto) {
        Client client = new Client();
        mapDtoToEntity(dto, client);
        clientRepository.save(client);
        return new Result("Mijoz qo'shildi", true);
    }

    public Result edit(Long id, ClientDto dto) {
        return clientRepository.findById(id).map(client -> {
            mapDtoToEntity(dto, client);
            clientRepository.save(client);
            return new Result("Mijoz tahrirlandi", true);
        }).orElse(new Result("Mijoz topilmadi", false));
    }

    public Result delete(Long id) {
        if (!clientRepository.existsById(id)) {
            return new Result("Mijoz topilmadi", false);
        }
        clientRepository.deleteById(id);
        return new Result("Mijoz o'chirildi", true);
    }

    private void mapDtoToEntity(ClientDto dto, Client client) {
        client.setClientName(dto.getClientName());
        client.setAddress(dto.getAddress());
        client.setPhone(dto.getPhone());
        client.setInn(dto.getInn());
        client.setEmail(dto.getEmail());
        client.setRegion(dto.getRegion());
        client.setState(dto.getState() != null ? dto.getState() : ClientState.ACTIVE);
        if (dto.getEmpId() != null) {
            userRepository.findById(dto.getEmpId())
                    .ifPresent(client::setEmployee);
        }
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(client::setCreatedBy);
        }
    }
}
