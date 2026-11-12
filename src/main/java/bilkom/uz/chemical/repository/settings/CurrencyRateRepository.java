package bilkom.uz.chemical.repository.settings;

import bilkom.uz.chemical.entity.settings.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> findAllByOrderByRateDateDescIdDesc();
    boolean existsByCurrencyId(Long currencyId);
}
