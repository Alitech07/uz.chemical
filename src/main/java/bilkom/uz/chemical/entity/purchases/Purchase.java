package bilkom.uz.chemical.entity.purchases;

import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.entity.products.Product;
import bilkom.uz.chemical.entity.suppliers.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "country")
    private String country;

    @Column(name = "brand")
    private String brand;

    @Column(name = "delivery_type")
    private String deliveryType;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "certificate_path")
    private String certificatePath;

    @Column(name = "purchase_description")
    private String purchaseDescription;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(precision = 15, scale = 3)
    private BigDecimal amount;

    @Column(name = "cost_price", precision = 15, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "transport_expenses", precision = 15, scale = 2)
    private BigDecimal transportExpenses;

    @Column(name = "customs_expenses", precision = 15, scale = 2)
    private BigDecimal customsExpenses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseStatus status = PurchaseStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseState state = PurchaseState.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
}
