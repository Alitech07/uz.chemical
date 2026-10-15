package bilkom.uz.chemical.service.products;

import bilkom.uz.chemical.dto.products.CategoryDto;
import bilkom.uz.chemical.dto.Result;
import bilkom.uz.chemical.entity.products.Category;
import bilkom.uz.chemical.repository.products.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Result getAll() {
        return new Result("OK", true, categoryRepository.findAll());
    }

    public Result getById(Long id) {
        return categoryRepository.findById(id)
                .map(c -> new Result("OK", true, c))
                .orElse(new Result("Kategoriya topilmadi", false));
    }

    public Result add(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        categoryRepository.save(category);
        return new Result("Kategoriya qo'shildi", true);
    }

    public Result edit(Long id, CategoryDto dto) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(dto.getName());
            category.setDescription(dto.getDescription());
            categoryRepository.save(category);
            return new Result("Kategoriya yangilandi", true);
        }).orElse(new Result("Kategoriya topilmadi", false));
    }

    public Result delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            return new Result("Kategoriya topilmadi", false);
        }
        categoryRepository.deleteById(id);
        return new Result("Kategoriya o'chirildi", true);
    }
}
