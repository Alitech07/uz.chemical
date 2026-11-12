package bilkom.uz.chemical.dto.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CurrencyRateDto {
    private Long currencyId;
    private BigDecimal rate;
    private LocalDateTime rateDate;
}
