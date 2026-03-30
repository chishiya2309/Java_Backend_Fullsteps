package vn.hunghaohan.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

/**
 * Load environment variables from .env file at application startup.
 * This allows Spring Boot to resolve ${ENV_VARIABLE} placeholders in application.yml files.
 */
@Configuration
public class DotEnvConfig {

    static {
        Dotenv dotenv = Dotenv
                .configure()
                .ignoreIfMissing()
                .load();

        // Set environment variables from .env file into System properties
        // so Spring Boot can resolve them via ${VAR_NAME} in YAML config files
        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
    }
}

