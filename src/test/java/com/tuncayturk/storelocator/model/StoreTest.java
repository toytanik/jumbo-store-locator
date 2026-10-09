package com.tuncayturk.storelocator.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import org.junit.jupiter.api.Test;

class StoreTest {

    private static final GeoPoint LOCATION = new GeoPoint(52.0907, 5.1214);

    @Test
    void shouldThrowNullPointerException_givenNullId() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Store(null, "name", "city", "1234 AB", "street", "1", LOCATION));
    }

    @Test
    void shouldThrowNullPointerException_givenNullLocation() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Store("id", "name", "city", "1234 AB", "street", "1", null));
    }

    @Test
    void shouldNormalizeNullNameToEmptyString() {
        Store store = new Store("id", null, "city", "1234 AB", "street", "1", LOCATION);

        assertThat(store.name()).isEmpty();
    }
}
