package org.project.storage.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Database implements Serializable {
    private String name;
    private Map<String, Collection> collections;
    private ObjectMapper objectMapper = new ObjectMapper();

    public Database(String name) {
        this.name = name;
        this.collections = new HashMap<>();
    }

    public String getDatabaseName() {
        return name;
    }

    public void addCollection(String collectionName) {
        if (!collections.containsKey(collectionName)) {
            collections.put(collectionName, new Collection(collectionName));
        } else {
            System.out.println("Collection already exists!");
        }
    }

    public Collection getCollection(String collectionName) {
        return collections.get(collectionName);
    }

    public void deleteCollection(String collectionName) {
        if (collections.containsKey(collectionName)) {
            collections.remove(collectionName);
        } else {
            System.out.println("Collection does not exist!");
        }
    }

    public JsonNode serializeDatabase() {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (Map.Entry<String, Collection> entry : collections.entrySet()) {
            Collection collection = entry.getValue();
            JsonNode documentNode = objectMapper.valueToTree(collection);
            arrayNode.add(documentNode);
        }

        return arrayNode;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Database Name: ").append(name).append("\n");
        sb.append("Number of Collections: ").append(collections.size()).append("\n");

        sb.append("Collections Data:\n");
        for (Map.Entry<String, Collection> entry : collections.entrySet()) {
            sb.append("Collection Name: ").append(entry.getKey()).append("\n");
            sb.append("Data: ").append(entry.getValue().toString()).append("\n");
            sb.append("------\n");
        }

        return sb.toString();
    }

}
