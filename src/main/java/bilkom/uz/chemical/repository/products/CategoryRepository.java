package bilkom.uz.chemical.repository.products;

import bilkom.uz.chemical.entity.products.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
