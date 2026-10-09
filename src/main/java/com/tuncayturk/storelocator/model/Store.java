package com.tuncayturk.storelocator.model;

import java.util.Objects;

/**
 * A Jumbo store location, mapped from the raw {@code stores.json} fields:
 * {@code uuid -> id}, {@code addressName -> name}, {@code street2 -> houseNumber}.
 * {@code street3} is not mapped because it is empty for every store.
 */
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
