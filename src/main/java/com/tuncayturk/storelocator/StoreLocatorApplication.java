package com.tuncayturk.storelocator;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@OpenAPIDefinition(info = @Info(
        title = "Jumbo Store Locator API",
        version = "v1",
        description = "Returns the Jumbo stores nearest to a given position."
))
@ConfigurationPropertiesScan
@SpringBootApplication
public class StoreLocatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreLocatorApplication.class, args);
    }
}
