package bilkom.uz.chemical.dto.clients;

import bilkom.uz.chemical.entity.clients.ClientState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private String clientName;
    private String address;
    private String phone;
    private String inn;
    private String email;
    private String region;
    private ClientState state;
    private Long empId;
}
