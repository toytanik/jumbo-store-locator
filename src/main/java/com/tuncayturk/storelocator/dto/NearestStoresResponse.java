package com.tuncayturk.storelocator.dto;

import java.util.List;

public record NearestStoresResponse(Origin origin, List<StoreResponse> stores) {
}
