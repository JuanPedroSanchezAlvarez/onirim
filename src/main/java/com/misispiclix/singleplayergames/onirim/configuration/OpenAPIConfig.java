package com.misispiclix.singleplayergames.onirim.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${onirim.openapi.dev-url}")
    private String devUrl;

    @Value("${onirim.openapi.pro-url}")
    private String prodUrl;

    private final String securitySchemeName = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {

        Contact contact = new Contact()
                .name("Juan Pedro Sánchez Álvarez")
                .url("https://www.juanpedrosanchezalvarez.com")
                .email("juanpedrosanchez1989@gmail.com");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("OpenAPI Course")
                .description("Specification for OpenAPI Course")
                .termsOfService("http://example.com/terms/")
                .contact(contact)
                .license(license)
                .version("1.0");

        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development Server");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Production Server");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);
        SecurityScheme securityScheme = new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        Components components = new Components().addSecuritySchemes(securitySchemeName, securityScheme);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .addSecurityItem(securityRequirement)
                .components(components);

        /*return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("spring_oauth", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Oauth2 flow")
                                .flows(new OAuthFlows()
                                        .clientCredentials(new OAuthFlow()
                                                .tokenUrl("http://localhost:8080" + "/oauth/token")
                                                .scopes(new Scopes()
                                                        .addString("read", "for read operations")
                                                        .addString("write", "for write operations")
                                                ))))
                )
                .security(Arrays.asList(
                        new SecurityRequirement().addList("spring_oauth")))
                .info(new Info()
                        .title("Book Application API")
                        .description("This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
                        .termsOfService("terms")
                        .contact(new Contact().email("codersitedev@gmail.com").name("Developer: Moises Gamio"))
                        .license(new License().name("GNU"))
                        .version("2.0")
                );*/
    }

}
