package bilkom.uz.chemical.repository.clients;

import bilkom.uz.chemical.entity.clients.Client;
import bilkom.uz.chemical.entity.clients.ClientState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByInn(String inn);
    List<Client> findAllByState(ClientState state);
    List<Client> findAllByRegion(String region);
    List<Client> findAllByEmployeeId(Long empId);
    List<Client> findAllByClientNameContainingIgnoreCase(String name);
}
