package bilkom.uz.chemical.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String login;
    private String password;
    private String isActive;
    private Set<Long> roleIds;
}
