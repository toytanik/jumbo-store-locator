package com.tuncayturk.storelocator.repository;

import com.tuncayturk.storelocator.model.Store;

import java.util.List;

public interface StoreRepository {
    List<Store> findAll();
}
