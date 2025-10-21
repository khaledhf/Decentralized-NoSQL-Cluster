package org.project.storage.models;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.project.configuration.DBConfig;
import org.project.configuration.Postman;
import org.project.storage.indexing.HashIndexer;
import java.io.*;
import java.util.*;


public class Document {
    private String name;
    private String id;
    private int affinity;
    private ObjectNode jsonNode;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private File file;

    public Document(String name) {
    this.name = name;
    this.id = UUID.randomUUID().toString();
    this.affinity = new Random().nextInt(DBConfig.NODES_NUMBER)+1;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getAffinity() {
        return affinity;
    }
    public ObjectNode getData() {
        return jsonNode;
    }
    public void setFile(File file) {
        this.file = file;
    }
    public Object getField(String key) {
        return jsonNode.get(key);
    }
    public boolean containsField(String key) {
        return jsonNode.has(key);
    }

    public JsonNode addDocument(PrintWriter sender, DataInputStream receiver,File file) throws IOException {
       if (jsonNode != null){
            HashIndexer.getInstance().deleteDocument(this);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        String attribute;
        Object value;
        data.put("affinity", new Random().nextInt(DBConfig.NODES_NUMBER)+1);
        data.put("id",UUID.randomUUID().toString());
        System.out.println("Enter your attribute/s and value/s and enter -exit- when finish:");
        Postman.sendMessage(sender,"Enter your attribute/s and value/s and enter -exit- when finish:");
        String newAttribute = receiver.readLine();
        do {
            attribute = newAttribute;
            value = receiver.readLine();
            data.put(attribute,value);
            newAttribute = receiver.readLine();
        }while (!newAttribute.equalsIgnoreCase("exit"));
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        objectWriter.writeValue(file, data);
        ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(file);
        HashIndexer.getInstance().add(jsonNode);
        System.out.println("adding Document has done successfully!");
        return jsonNode;
    }
    public void readDataFromFile(File file){
        try {
            this.file = file;
            jsonNode = (ObjectNode) objectMapper.readTree(file);
            id = jsonNode.get("id").asText();
            affinity = jsonNode.get("affinity").asInt();
            HashIndexer.getInstance().add(jsonNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void removeField(String key) throws IOException {
        if (jsonNode.has(key)) {
            jsonNode.remove(key);

            HashIndexer.getInstance().deleteDocument(this);
            ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
            objectWriter.writeValue(file, jsonNode);

            HashIndexer.getInstance().add(jsonNode);
        } else {
            System.out.println("Key doesn't exist in the document.");
        }
    }

    public File getFile() {
        return file;
    }

    public void replaceField(String attribute, String value) throws IOException {
        if (jsonNode.has(attribute)) {
            jsonNode.put(attribute, value);
            HashIndexer.getInstance().deleteDocument(this);
            ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
            objectWriter.writeValue(file, jsonNode);
            HashIndexer.getInstance().add(jsonNode);
        } else {
            System.out.println("Attribute doesn't exist. Use addData() method to add a new attribute.");
        }
    }

    public void setJsonNode(ObjectNode jsonNode) {
        HashIndexer.getInstance().add(jsonNode);
        this.jsonNode = jsonNode;
    }

    public void addField(String attribute, String value) {
        try {
            jsonNode.put(attribute, value);
            HashIndexer.getInstance().deleteDocument(this);

            ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
            objectWriter.writeValue(file, jsonNode);

            HashIndexer.getInstance().add(jsonNode);
        } catch (IOException e) {
            System.err.println("Failed to add field to document. Error: " + e.getMessage());
        }
    }
    public JsonNode readDocument() {
            return jsonNode;
    }

    @Override
    public String toString() {
        return String.valueOf(jsonNode);
    }
}

