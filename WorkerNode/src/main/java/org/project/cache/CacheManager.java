package org.project.cache;

import com.fasterxml.jackson.databind.JsonNode;

public class CacheManager {

    private static LRUCache<String, Object> instance;

    public static synchronized LRUCache<String, Object> getInstance() {
        if (instance == null) {
            instance = new LRUCache<>(100);
        }
        return instance;
    }
}
