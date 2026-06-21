package bilkom.uz.chemical.entity.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String label;

    @Column(name = "order_num", nullable = false)
    private Integer orderNum = 0;
}
