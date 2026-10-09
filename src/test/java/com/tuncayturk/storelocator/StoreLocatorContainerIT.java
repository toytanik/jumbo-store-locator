package com.tuncayturk.storelocator;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@Testcontainers
class StoreLocatorContainerIT {

    @Container
    private static final GenericContainer<?> container = new GenericContainer<>(buildImage())
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200).withStartupTimeout(Duration.ofSeconds(90)));

    private final HttpClient client = HttpClient.newHttpClient();

    private static ImageFromDockerfile buildImage() {
        Path jar = findBuiltJar();
        return new ImageFromDockerfile("jumbo-store-locator-it", true)
                .withFileFromPath("Dockerfile", Path.of("Dockerfile"))
                .withFileFromPath("target/" + jar.getFileName(), jar);
    }

    private static Path findBuiltJar() {
        Path targetDir = Path.of("target");
        try (var files = Files.list(targetDir)) {
            return files
                    .filter(p -> p.getFileName().toString().matches("jumbo-store-locator-.*\\.jar"))
                    .filter(p -> !p.getFileName().toString().endsWith(".original"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "No jar found in target/ - run `./mvnw package` before this test"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Test
    void shouldReturnFiveStoresSortedByDistance_givenCentralUtrecht() throws Exception {
        HttpResponse<String> response = get("/api/v1/stores/nearest?latitude=52.0907&longitude=5.1214");

        assertThat(response.statusCode()).isEqualTo(200);

        JsonNode stores = JsonMapper.builder().build().readTree(response.body()).path("stores");
        JsonAssertions.assertSortedAscendingByDistance(stores);
    }

    @Test
    void shouldReturn400_givenInvalidLatitude() throws Exception {
        HttpResponse<String> response = get("/api/v1/stores/nearest?latitude=91&longitude=5.1214");

        assertThat(response.statusCode()).isEqualTo(400);
    }

    private HttpResponse<String> get(String path) throws Exception {
        String baseUrl = "http://%s:%d".formatted(container.getHost(), container.getMappedPort(8080));
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
