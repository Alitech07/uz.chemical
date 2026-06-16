package bilkom.uz.chemical.service.products;

import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.dto.products.ProductDto;
import bilkom.uz.chemical.entity.products.Product;
import bilkom.uz.chemical.entity.products.ProductState;
import bilkom.uz.chemical.repository.UserRepository;
import bilkom.uz.chemical.repository.products.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Result getAll() {
        return new Result("OK", true, productRepository.findAll());
    }

    public Result getById(Long id) {
        return productRepository.findById(id)
                .map(p -> new Result("OK", true, p))
                .orElse(new Result("Mahsulot topilmadi", false));
    }

    public Result search(String name) {
        return new Result("OK", true,
                productRepository.findAllByProductNameContainingIgnoreCase(name));
    }

    public Result getByState(ProductState state) {
        return new Result("OK", true, productRepository.findAllByState(state));
    }

    public Result add(ProductDto dto) {
        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setComposition(dto.getComposition());
        product.setMeasure(dto.getMeasure());
        product.setState(dto.getState() != null ? dto.getState() : ProductState.ACTIVE);
        if (dto.getCreatedById() != null) {
            userRepository.findById(dto.getCreatedById())
                    .ifPresent(product::setCreatedBy);
        }
        productRepository.save(product);
        return new Result("Mahsulot qo'shildi", true);
    }

    public Result edit(Long id, ProductDto dto) {
        return productRepository.findById(id).map(product -> {
            product.setProductName(dto.getProductName());
            product.setComposition(dto.getComposition());
            product.setMeasure(dto.getMeasure());
            if (dto.getState() != null) product.setState(dto.getState());
            productRepository.save(product);
            return new Result("Mahsulot tahrirlandi", true);
        }).orElse(new Result("Mahsulot topilmadi", false));
    }

    public Result delete(Long id) {
        if (!productRepository.existsById(id)) {
            return new Result("Mahsulot topilmadi", false);
        }
        productRepository.deleteById(id);
        return new Result("Mahsulot o'chirildi", true);
    }
}
