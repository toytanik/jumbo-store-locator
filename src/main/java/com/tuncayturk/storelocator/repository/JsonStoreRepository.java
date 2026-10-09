package com.tuncayturk.storelocator.repository;

import com.tuncayturk.storelocator.config.StoreLocatorProperties;
import com.tuncayturk.storelocator.model.Store;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Slf4j
@Repository
public class JsonStoreRepository implements StoreRepository {


    private final List<Store> stores;

    public JsonStoreRepository(StoreLocatorProperties properties, JsonMapper jsonMapper) {
        this.stores = List.copyOf(load(properties.dataLocation(), jsonMapper));
    }

    @Override
    public List<Store> findAll() {
        return stores;
    }

    private List<Store> load(Resource resource, JsonMapper jsonMapper) {
        if (!resource.exists()) {
            throw new IllegalStateException("Required resource not found: " + resource);
        }

        long startedAt = System.nanoTime();
        StoresFile file;
        try (InputStream in = resource.getInputStream()) {
            file = jsonMapper.readValue(in, StoresFile.class);
        } catch (IOException | JacksonException e) {
            throw new IllegalStateException("Failed to read " + resource, e);
        }

        List<Store> loaded = new ArrayList<>();
        int skipped = 0;
        for (StoreJson entry : file.stores()) {
            try {
                loaded.add(StoreJsonMapper.toDomain(entry));
            } catch (RuntimeException e) {
                skipped++;
                String uuid = entry.uuid() != null ? entry.uuid() : "<unknown>";
                log.warn("Skipping malformed store uuid={}: {}", uuid, e.getMessage());
            }
        }

        long tookMs = (System.nanoTime() - startedAt) / 1_000_000;
        log.info("Loaded {} stores ({} skipped) in {} ms", loaded.size(), skipped, tookMs);
        return loaded;
    }
}
