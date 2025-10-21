package org.project.storage.indexing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.project.storage.models.Document;

import java.util.*;


public class HashIndexer {
    private static HashIndexer instance ;
    private static Map<String, AttributeIndex> map;
    private HashIndexer() {
        this.map = new HashMap<>();
    }
    public static HashIndexer getInstance() {
        if (instance == null) {
            instance = new HashIndexer();
        }
        return instance;
    }
    public Map<String, AttributeIndex> getMap() {
        return map;
    }

    public void add(ObjectNode jsonNode) {
        Iterator<String> iterator = jsonNode.fieldNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = jsonNode.get(key).asText();
            map.computeIfAbsent(key, k -> new AttributeIndex()).addValue(value, jsonNode);
        }
    }

    public String search(String attribute, String value) {
        if (attribute == null || value == null) {
            throw new IllegalArgumentException("Attribute or value can't be null.");
        }

        List<JsonNode> nodes = map.getOrDefault(attribute, new AttributeIndex()).getNodes(value);
        if (nodes.isEmpty()) {
            return "There is no value like this in indexer";
        }

        return nodes.toString();
    }


    public List<JsonNode> searchByAttribute(String attribute, String value) {
        if (attribute == null || value == null) {
            throw new IllegalArgumentException("Attribute or value can't be null.");
        }
        return map.getOrDefault(attribute, new AttributeIndex()).getNodes(value);
    }

    public void deleteDocuments(List<Document> documents) {
        for (Document document : documents) {
            deleteDocument(document);
        }
    }

    public void clearIndex() {
        map.clear();
    }

    public void deleteDocument(Document document) {
        try {
            JsonNode jsonNode = document.readDocument();
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonNode.get(key).textValue();

                AttributeIndex attributeIndex = map.get(key);
                if (attributeIndex != null) {
                    attributeIndex.removeSpecificDocument(value, jsonNode);

                    if (attributeIndex.isEmpty()) {
                        map.remove(key);
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("The document is empty");
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashIndexer:\n");

        for (Map.Entry<String, AttributeIndex> outerEntry : map.entrySet()) {
            sb.append("Attribute: ").append(outerEntry.getKey()).append("\n");

            AttributeIndex attributeIndex = outerEntry.getValue();
            for (String value : attributeIndex.getAllValues()) {
                sb.append("\tValue: ").append(value).append("\n");
                for (JsonNode node : attributeIndex.getNodes(value)) {
                    sb.append("\t\tFound in Document: ").append(node.toString()).append("\n");
                }
            }
        }

        return sb.toString();
    }


}
