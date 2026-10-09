package com.tuncayturk.storelocator.controller;

import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.tuncayturk.storelocator.dto.StoreResponse;
import com.tuncayturk.storelocator.model.GeoPoint;
import com.tuncayturk.storelocator.model.Store;
import com.tuncayturk.storelocator.model.StoreDistance;
import com.tuncayturk.storelocator.service.NearestStoreService;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private NearestStoreService nearestStoreService;

    @Test
    void shouldReturn200WithOrigin_givenValidCoordinates() {
        given(nearestStoreService.findNearest(any(GeoPoint.class), anyInt()))
                .willReturn(storesWithDistance(1));

        mvc.get().uri("/api/v1/stores/nearest?latitude=52.0907&longitude=5.1214")
                .exchange()
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.origin.latitude").isEqualTo(52.0907);
    }

    @Test
    void shouldReturnFiveStores_givenNoLimit() {
        given(nearestStoreService.findNearest(any(GeoPoint.class), anyInt()))
                .willReturn(storesWithDistance(5));

        mvc.get().uri("/api/v1/stores/nearest?latitude=52.0907&longitude=5.1214")
                .exchange()
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.stores").asInstanceOf(LIST)
                .hasSize(5);
    }

    @Test
    void shouldReturnTenStores_givenLimit10() {
        given(nearestStoreService.findNearest(any(GeoPoint.class), anyInt()))
                .willReturn(storesWithDistance(10));

        mvc.get().uri("/api/v1/stores/nearest?latitude=52.0907&longitude=5.1214&limit=10")
                .exchange()
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.stores").asInstanceOf(LIST)
                .hasSize(10);
    }

    @Test
    void shouldReturn400WithProblemDetailBody_givenLatitudeOutOfRange() {
        mvc.get().uri("/api/v1/stores/nearest?latitude=91&longitude=5.1214")
                .exchange()
                .assertThat()
                .hasStatus(400)
                .bodyJson()
                .extractingPath("$.status").isEqualTo(400);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRequests")
    void shouldReturn400_givenInvalidRequest(String description, String queryString) {
        mvc.get().uri("/api/v1/stores/nearest?" + queryString)
                .exchange()
                .assertThat()
                .hasStatus(400);
    }

    private static Stream<Arguments> invalidRequests() {
        return Stream.of(
                Arguments.of("missing latitude", "longitude=5.1214"),
                Arguments.of("missing longitude", "latitude=52.0907"),
                Arguments.of("longitude out of range", "latitude=52.0907&longitude=-181"),
                Arguments.of("non-numeric latitude", "latitude=not-a-number&longitude=5.1214"),
                Arguments.of("NaN latitude", "latitude=NaN&longitude=5.1214"),
                Arguments.of("infinite latitude", "latitude=Infinity&longitude=5.1214"),
                Arguments.of("limit zero", "latitude=52.09&longitude=5.12&limit=0"),
                Arguments.of("limit too large", "latitude=52.09&longitude=5.12&limit=51"),
                Arguments.of("non-numeric limit", "latitude=52.09&longitude=5.12&limit=abc")
        );
    }

    private static List<StoreResponse> storesWithDistance(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new StoreDistance(
                        new Store("id-" + i, "Jumbo " + i, "Utrecht", "3511 AB", "Street", "1",
                                new GeoPoint(52.0907, 5.1214)),
                        i * 0.1))
                .map(StoreResponse::of)
                .toList();
    }
}
