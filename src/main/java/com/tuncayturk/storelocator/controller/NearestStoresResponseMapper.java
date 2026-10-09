package com.tuncayturk.storelocator.controller;

import com.tuncayturk.storelocator.dto.NearestStoresResponse;
import com.tuncayturk.storelocator.dto.Origin;
import com.tuncayturk.storelocator.dto.StoreResponse;
import com.tuncayturk.storelocator.model.GeoPoint;

import java.util.List;

final class NearestStoresResponseMapper {

    private NearestStoresResponseMapper() {
    }

    static NearestStoresResponse toResponse(GeoPoint origin, List<StoreResponse> stores) {
        return new NearestStoresResponse(
                new Origin(origin.latitude(), origin.longitude()),
                stores
        );
    }
}
