package com.tuncayturk.storelocator.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tuncayturk.storelocator.config.StoreLocatorProperties;
import com.tuncayturk.storelocator.model.Store;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.json.JsonMapper;

class JsonStoreRepositoryTest {

    private final JsonStoreRepository repository = new JsonStoreRepository(
            new StoreLocatorProperties(new ClassPathResource("stores-test.json")),
            JsonMapper.builder().build());

    @Test
    void shouldLoadOnlyWellFormedRows_givenFixtureWithMalformedRows() {
        assertThat(repository.findAll()).hasSize(3);
    }

    @Test
    void shouldSkipMalformedRow_givenNonNumericLatitude() {
        List<Store> stores = repository.findAll();

        assertThat(stores).noneMatch(store -> store.id().equals("malformed-latitude"));
    }

    @Test
    void shouldSkipMalformedRow_givenNaNLatitude() {
        List<Store> stores = repository.findAll();

        assertThat(stores).noneMatch(store -> store.id().equals("nan-latitude"));
    }

    @Test
    void shouldParseStringLatitudeAndLongitudeAsDoubles() {
        Store store = repository.findAll().stream()
                .filter(s -> s.id().equals("valid-1"))
                .findFirst()
                .orElseThrow();

        assertThat(store.location().latitude()).isEqualTo(52.0907);
        assertThat(store.location().longitude()).isEqualTo(5.1214);
    }

    @Test
    void shouldThrowIllegalStateException_givenMissingResource() {
        StoreLocatorProperties properties = new StoreLocatorProperties(new ClassPathResource("does-not-exist.json"));
        JsonMapper jsonMapper = JsonMapper.builder().build();

        assertThatThrownBy(() -> new JsonStoreRepository(properties, jsonMapper))
                .isInstanceOf(IllegalStateException.class);
    }
}
