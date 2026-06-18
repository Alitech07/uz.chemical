package bilkom.uz.chemical.entity.products;

import bilkom.uz.chemical.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String composition;

    @Column(nullable = false)
    private String measure;

    @Column(name = "country_manufacture")
    private String countryManufacture;

    @Column(name = "manufacturer_name")
    private String manufacturerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductState state = ProductState.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
}
