package com.misispiclix.singleplayergames.onirim.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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

    @Bean
    public OpenAPI myOpenAPI() {

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

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }

}
