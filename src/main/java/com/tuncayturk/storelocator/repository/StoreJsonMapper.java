package com.tuncayturk.storelocator.repository;

import com.tuncayturk.storelocator.model.GeoPoint;
import com.tuncayturk.storelocator.model.Store;

final class StoreJsonMapper {

    private StoreJsonMapper() {
    }

    static Store toDomain(StoreJson json) {
        String uuid = requireText(json.uuid(), "uuid");
        GeoPoint location = new GeoPoint(
                parseCoordinate(json.latitude(), "latitude"),
                parseCoordinate(json.longitude(), "longitude")
        );

        return new Store(
                uuid,
                json.addressName(),
                json.city(),
                json.postalCode(),
                json.street(),
                json.street2(),
                location
        );
    }

    private static double parseCoordinate(String value, String field) {
        requireText(value, field);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid " + field + ": " + value, e);
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("missing required field: " + field);
        }
        return value;
    }
}
