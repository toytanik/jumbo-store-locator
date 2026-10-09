package com.tuncayturk.storelocator.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
record StoresFile(List<StoreJson> stores) {
}
