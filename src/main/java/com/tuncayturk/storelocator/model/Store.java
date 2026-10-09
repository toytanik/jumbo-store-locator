package com.tuncayturk.storelocator.model;

import java.util.Objects;

public record Store(
        String id,
        String name,
        String city,
        String postalCode,
        String street,
        String houseNumber,
        GeoPoint location
) {

    public Store {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(location, "location");
        name = Objects.requireNonNullElse(name, "");
        city = Objects.requireNonNullElse(city, "");
        postalCode = Objects.requireNonNullElse(postalCode, "");
        street = Objects.requireNonNullElse(street, "");
        houseNumber = Objects.requireNonNullElse(houseNumber, "");
    }
}
