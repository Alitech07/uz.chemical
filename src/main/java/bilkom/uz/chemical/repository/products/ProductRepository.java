package bilkom.uz.chemical.repository.products;

import bilkom.uz.chemical.entity.products.Product;
import bilkom.uz.chemical.entity.products.ProductState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByState(ProductState state);
    List<Product> findAllByProductNameContainingIgnoreCase(String name);
}
