package com.tuncayturk.storelocator;

import static org.assertj.core.api.Assertions.assertThat;

import tools.jackson.databind.JsonNode;

final class JsonAssertions {

    private JsonAssertions() {
    }

    static void assertSortedAscendingByDistance(JsonNode stores) {
        assertThat(stores.size()).isEqualTo(5);

        double previousDistance = -1;
        for (JsonNode store : stores) {
            double distance = store.path("distanceKm").asDouble();
            assertThat(distance).isGreaterThanOrEqualTo(previousDistance);
            previousDistance = distance;
        }
    }
}
