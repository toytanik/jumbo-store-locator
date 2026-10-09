package com.tuncayturk.storelocator.service;

import com.tuncayturk.storelocator.dto.StoreResponse;
import com.tuncayturk.storelocator.geo.DistanceCalculator;
import com.tuncayturk.storelocator.model.GeoPoint;
import com.tuncayturk.storelocator.model.StoreDistance;
import com.tuncayturk.storelocator.repository.StoreRepository;
import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NearestStoreService {

    private final StoreRepository storeRepository;

    public List<StoreResponse> findNearest(GeoPoint origin, int limit) {
        List<StoreResponse> result = storeRepository.findAll().stream()
                .map(store -> new StoreDistance(store, DistanceCalculator.distanceKm(origin, store.location())))
                .sorted(Comparator.comparingDouble(StoreDistance::distanceKm)
                        .thenComparing(sd -> sd.store().id()))
                .limit(limit)
                .map(StoreResponse::of)
                .toList();

        log.debug("findNearest origin={} limit={} returned={}", origin, limit, result.size());
        return result;
    }
}
