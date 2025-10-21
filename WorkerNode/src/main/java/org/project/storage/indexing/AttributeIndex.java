package org.project.storage.indexing;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.*;

public class AttributeIndex {
    private Map<String, List<JsonNode>> valueIndex = new HashMap<>();
    public void addValue(String value, JsonNode node) {
        valueIndex.computeIfAbsent(value, k -> new ArrayList<>()).add(node);
    }

    public List<JsonNode> getNodes(String value) {
        return valueIndex.getOrDefault(value, Collections.emptyList());
    }
    public Set<String> getAllValues() {
        return valueIndex.keySet();
    }
    public boolean isEmpty() {
        return valueIndex.isEmpty();
    }
    public void removeSpecificDocument(String value, JsonNode node) {
        List<JsonNode> nodes = valueIndex.get(value);
        if (nodes != null) {
            nodes.remove(node);
            if (nodes.isEmpty()) {
                valueIndex.remove(value);
            }
        }
    }
}
