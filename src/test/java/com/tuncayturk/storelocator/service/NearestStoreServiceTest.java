package com.tuncayturk.storelocator.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.tuncayturk.storelocator.dto.StoreResponse;
import com.tuncayturk.storelocator.model.GeoPoint;
import com.tuncayturk.storelocator.model.Store;
import com.tuncayturk.storelocator.repository.StoreRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class NearestStoreServiceTest {

    private static final Store UTRECHT = store("utrecht", 52.09, 5.12);
    private static final Store HOUTEN = store("houten", 52.09, 5.22);
    private static final Store BUNNIK = store("bunnik", 52.09, 5.32);
    private static final Store ZEIST = store("zeist", 52.09, 5.42);

    private final GeoPoint origin = new GeoPoint(52.09, 5.12);

    @Test
    void shouldReturnStoresOrderedByAscendingDistance() {
        NearestStoreService service = serviceWith(ZEIST, UTRECHT, BUNNIK, HOUTEN);

        List<StoreResponse> result = service.findNearest(origin, 10);

        assertThat(result).extracting(StoreResponse::id)
                .containsExactly("utrecht", "houten", "bunnik", "zeist");
        assertThat(result).isSortedAccordingTo((a, b) -> Double.compare(a.distanceKm(), b.distanceKm()));
    }

    @Test
    void shouldReturnExactlyLimitStores_givenMoreStoresThanLimit() {
        NearestStoreService service = serviceWith(UTRECHT, HOUTEN, BUNNIK, ZEIST);

        List<StoreResponse> result = service.findNearest(origin, 2);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(StoreResponse::id).containsExactly("utrecht", "houten");
    }

    @Test
    void shouldReturnAllStores_givenFewerStoresThanLimit() {
        NearestStoreService service = serviceWith(UTRECHT, HOUTEN);

        List<StoreResponse> result = service.findNearest(origin, 10);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(StoreResponse::id).containsExactly("utrecht", "houten");
    }

    @Test
    void shouldBreakTiesByStoreId_givenEqualDistances() {
        Store first = store("first", 52.5, 5.5);
        Store second = store("second", 52.5, 5.5);
        NearestStoreService service = serviceWith(second, first);

        List<StoreResponse> result = service.findNearest(new GeoPoint(52.09, 5.12), 10);

        assertThat(result).extracting(StoreResponse::id).containsExactly("first", "second");
    }

    private static NearestStoreService serviceWith(Store... stores) {
        StoreRepository repository = () -> List.of(stores);
        return new NearestStoreService(repository);
    }

    private static Store store(String id, double lat, double lon) {
        return new Store(id, "Jumbo " + id, "City", "1234 AB", "Street", "1", new GeoPoint(lat, lon));
    }
}
