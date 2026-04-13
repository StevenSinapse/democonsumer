package nc.sinapse.kafka.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
public class ProjectionSchemaConfig {

    private final JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner ensureProjectionSchema() {
        return args -> jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS demo_consumer");
    }
}
