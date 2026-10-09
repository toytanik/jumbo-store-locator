package com.tuncayturk.storelocator.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record StoreJson(
        String uuid,
        String addressName,
        String city,
        String postalCode,
        String street,
        String street2,
        String latitude,
        String longitude
) {
}
