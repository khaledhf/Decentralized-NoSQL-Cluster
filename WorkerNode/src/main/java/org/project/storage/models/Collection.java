package org.project.storage.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Collection implements Serializable {
    private String name;
    private Map<String, Document> documents;
    private ObjectMapper objectMapper = new ObjectMapper();


    public Collection(String name){
        this.name = name;
        this.documents = new HashMap<>();
    }
    public void addDocument(String documentName ){
        documents.put(documentName,new Document(documentName));
    }
    public void deleteDocument(String documentName) {
        documents.remove(documentName);
    }
    public Document getDocument(String documentName) {
        if( documents.get(documentName) == null)
            new Document(documentName);
        return documents.get(documentName);
    }
    public JsonNode getCollection() {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (Map.Entry<String, Document> entry : documents.entrySet()) {
            Document doc = entry.getValue();
            JsonNode documentNode = objectMapper.valueToTree(doc);
            arrayNode.add(documentNode);
        }

        return arrayNode;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Collection {");
        stringBuilder.append("\n\tname: ").append(name);
        stringBuilder.append("\n\tdocuments: {\n");

        for (Map.Entry<String, Document> entry : documents.entrySet()) {
            stringBuilder.append("\t\t").append(entry.getValue().getData()).append("\n");
        }
        stringBuilder.append("\t}\n");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

}
