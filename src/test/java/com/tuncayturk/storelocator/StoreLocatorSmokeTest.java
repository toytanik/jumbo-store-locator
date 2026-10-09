package com.tuncayturk.storelocator;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreLocatorSmokeTest {

    @LocalServerPort
    private int port;

    @Test
    void shouldReturnFiveStoresSortedByDistance_givenCentralUtrecht() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:%d/api/v1/stores/nearest?latitude=52.0907&longitude=5.1214".formatted(port)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);

        JsonNode body = JsonMapper.builder().build().readTree(response.body());
        JsonAssertions.assertSortedAscendingByDistance(body.path("stores"));
    }
}
