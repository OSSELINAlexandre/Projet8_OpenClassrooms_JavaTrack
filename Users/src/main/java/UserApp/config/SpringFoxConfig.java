package UserApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * This configuration class enable the usage of Swagger 2 endpoints.
 * Swagger 2 documents the application endpoints.
 *
 * Therefore, the http://localhost:8080/v2/api-docs and http://localhost:8080/swagger-ui.html
 * can be access.
 *
 *
 */
@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .paths(PathSelectors.regex("(?!/error.*).*"))
                .apis(RequestHandlerSelectors
                        .basePackage("UserApp"))
                .build();
    }
}
