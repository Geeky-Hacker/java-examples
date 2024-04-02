package com.geekyhacker.mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PropertyParameter {
    private final Cache cache;

    public PropertyParameter(Cache cache) {
        this.cache = cache;
    }

    public Optional<String> retrieve(String key) {
        return Optional.ofNullable(cache.get(key));
    }

    public static class Cache {
        private final Map<String, String> store;

        public Cache() {
            this.store = new HashMap<>();
        }

        public String get(String key) {
            return store.get(key);
        }

        public void set(String key, String value) {
            store.put(key, value);
        }
    }
}