package bilkom.uz.chemical.config;

import bilkom.uz.chemical.entity.User;
import bilkom.uz.chemical.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        try {
            if (userRepository.findByLogin("admin").isEmpty()) {
                User admin = new User();
                admin.setLogin("admin");
                admin.setFullname("Admin User");
                admin.setPassword(passwordEncoder.encode("Admin1234"));
                admin.setIsActive("A");
                userRepository.save(admin);
                System.out.println("=== Default admin user created: login=admin, password=Admin1234 ===");
            } else {
                System.out.println("=== Admin user already exists ===");
            }
        } catch (Exception e) {
            System.err.println("=== DataInitializer ERROR: " + e.getMessage() + " ===");
            e.printStackTrace();
        }
        initGrid13();
        migrateCurrencyRates();
    }

    // ddl-auto=update cannot drop constraints or change column types
    private void migrateCurrencyRates() {
        try {
            jdbcTemplate.queryForList(
                "SELECT conname FROM pg_constraint " +
                "WHERE conrelid = to_regclass('currency_rates') AND contype = 'u'", String.class)
                .forEach(name -> jdbcTemplate.execute(
                    "ALTER TABLE currency_rates DROP CONSTRAINT \"" + name + "\""));

            Integer dateCols = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.columns " +
                "WHERE table_name = 'currency_rates' AND column_name = 'rate_date' AND data_type = 'date'",
                Integer.class);
            if (dateCols != null && dateCols > 0) {
                jdbcTemplate.execute(
                    "ALTER TABLE currency_rates ALTER COLUMN rate_date TYPE timestamp USING rate_date::timestamp");
            }
        } catch (Exception e) {
            System.err.println("=== currency_rates migration ERROR: " + e.getMessage() + " ===");
        }
    }

    private void initGrid13() {
        try {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM core.grids WHERE grid_id = 13", Integer.class);
            if (count != null && count > 0) return;

            String config = """
                {
                  "page": 20,
                  "search": false,
                  "filter": false,
                  "checkbox": false,
                  "showFile": false,
                  "showFooter": true,
                  "groups": [
                    {"key": "product_info", "label": "MAHSULOT MA'LUMOTLARI"}
                  ]
                }
                """;

            String columns = """
                [
                  {"key":"sana",           "label":"SANA",                                   "type":"date",   "exportable":true,  "visible":true},
                  {"key":"zayavka_no",     "label":"ZAYAVKA №",                              "type":"text",   "exportable":true,  "visible":true},
                  {"key":"direction",      "label":"YO'NALISH",                              "type":"text",   "exportable":true,  "visible":true},
                  {"key":"pay_type",       "label":"TO'LOV TURI",                            "type":"text",   "exportable":true,  "visible":true},
                  {"key":"product_name",   "label":"MAHSULOT NOMI",                          "type":"text",   "exportable":true,  "visible":true,  "group":"product_info"},
                  {"key":"brand",          "label":"KORKONA BRENDI",                         "type":"text",   "exportable":true,  "visible":true,  "group":"product_info"},
                  {"key":"country",        "label":"MAHSULOT ISHLAB CHIQARILGAN DAVLATI",    "type":"text",   "exportable":true,  "visible":true,  "group":"product_info"},
                  {"key":"logistics_name", "label":"LOGISTIK KORKONA NOMI",                  "type":"text",   "exportable":true,  "visible":true},
                  {"key":"price",          "label":"NARXI",                                  "type":"number", "exportable":true,  "visible":true,  "footer":"sum"},
                  {"key":"quantity",       "label":"MIQDORI",                                "type":"number", "exportable":true,  "visible":true,  "footer":"sum"},
                  {"key":"total_value",    "label":"UMUMIY QIYMATI",                         "type":"number", "exportable":true,  "visible":true,  "footer":"sum"}
                ]
                """;

            jdbcTemplate.update(
                "INSERT INTO core.grids (grid_id, view_name, grid_config, grid_columns, state) VALUES (13, 'purchase_companies_v', ?::jsonb, ?::jsonb, 'A')",
                config, columns
            );
            System.out.println("=== Grid 13 (purchase_companies_v) created ===");
        } catch (Exception e) {
            System.err.println("=== Grid 13 init ERROR: " + e.getMessage() + " ===");
        }
    }
}
