package bilkom.uz.chemical.entity.admin;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String label;

    @Column(nullable = false)
    private String route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "order_num")
    private Integer orderNum = 0;

    @Column(nullable = false)
    private String state = "ACTIVE";
}
