package com.tuncayturk.storelocator.model;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.Test;

class GeoPointTest {

    @Test
    void shouldAccept_givenLowerBoundaryCoordinates() {
        assertThatNoException().isThrownBy(() -> new GeoPoint(-90, -180));
    }

    @Test
    void shouldAccept_givenUpperBoundaryCoordinates() {
        assertThatNoException().isThrownBy(() -> new GeoPoint(90, 180));
    }

    @Test
    void shouldReject_givenLatitudeJustAboveUpperBound() {
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(90.0001, 0));
    }

    @Test
    void shouldReject_givenLongitudeJustBelowLowerBound() {
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(0, -180.0001));
    }

    @Test
    void shouldReject_givenNaNLatitude() {
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(Double.NaN, 0));
    }

    @Test
    void shouldReject_givenNaNLongitude() {
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(0, Double.NaN));
    }

    @Test
    void shouldReject_givenInfiniteLatitude() {
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(Double.POSITIVE_INFINITY, 0));
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(Double.NEGATIVE_INFINITY, 0));
    }

    @Test
    void shouldReject_givenInfiniteLongitude() {
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(0, Double.POSITIVE_INFINITY));
        assertThatIllegalArgumentException().isThrownBy(() -> new GeoPoint(0, Double.NEGATIVE_INFINITY));
    }
}
