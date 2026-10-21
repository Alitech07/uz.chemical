package bilkom.uz.chemical.entity.settings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_type_id")
    private Long id;

    @Column(nullable = false)
    private String name;
}
