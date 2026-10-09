package com.tuncayturk.storelocator.dto;

import com.tuncayturk.storelocator.model.Store;
import com.tuncayturk.storelocator.model.StoreDistance;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record StoreResponse(
        String id,
        String name,
        String city,
        String postalCode,
        String address,
        double latitude,
        double longitude,
        double distanceKm
) {

    public static StoreResponse of(StoreDistance storeDistance) {
        Store store = storeDistance.store();
        return new StoreResponse(
                store.id(),
                store.name(),
                store.city(),
                store.postalCode(),
                formatAddress(store),
                store.location().latitude(),
                store.location().longitude(),
                roundToTwoDecimals(storeDistance.distanceKm()));
    }

    private static String formatAddress(Store store) {
        return Stream.of(store.street(), store.houseNumber())
                .filter(part -> !part.isBlank())
                .collect(Collectors.joining(" "));
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
