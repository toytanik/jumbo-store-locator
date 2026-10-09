package com.tuncayturk.storelocator.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import com.tuncayturk.storelocator.model.GeoPoint;
import org.junit.jupiter.api.Test;

class DistanceCalculatorTest {

    @Test
    void shouldReturnZeroDistance_givenSameLocationTwice() {
        GeoPoint point = new GeoPoint(52.0907, 5.1214);

        assertThat(DistanceCalculator.distanceKm(point, point)).isZero();
    }

    @Test
    void shouldReturnApproximately35Km_givenAmsterdamCentraalAndUtrechtCentraal() {
        GeoPoint amsterdamCentraal = new GeoPoint(52.3791, 4.9003);
        GeoPoint utrechtCentraal = new GeoPoint(52.0894, 5.1100);

        double distance = DistanceCalculator.distanceKm(amsterdamCentraal, utrechtCentraal);

        assertThat(distance).isCloseTo(35.0, offset(1.0));
    }

    @Test
    void shouldReturnSameDistance_givenSwappedPoints() {
        GeoPoint a = new GeoPoint(52.3791, 4.9003);
        GeoPoint b = new GeoPoint(52.0894, 5.1100);

        assertThat(DistanceCalculator.distanceKm(a, b)).isEqualTo(DistanceCalculator.distanceKm(b, a));
    }
}
