package org.project.authentication;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.JsonNode;
import org.project.broadcast.BroadcastSender;
import org.project.cache.CacheManager;
import org.project.cache.LRUCache;
import org.project.configuration.Query;
import org.project.storage.handlers.CRUDHandler;
import org.project.storage.handlers.CollectionHandler;
import org.project.storage.handlers.DatabaseHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UserRole implements Roles {
    private DataInputStream receiver;
    private PrintWriter sender;
    private  ObjectOutputStream sendObject;
    private LRUCache<String, Object> cache;

    public UserRole(DataInputStream receiver, PrintWriter sender, ObjectOutputStream sendObject) {
        this.receiver = receiver;
        this.sender = sender;
        this.sendObject = sendObject;
        this.cache = CacheManager.getInstance();
    }
    @Override
    public void start() {
        boolean anotherQuery = true;
        try {
            while (anotherQuery) {
                int choice = Integer.parseInt(receiver.readLine());
                switch (choice) {
                    case 1 -> handleCreate();
                    case 2 -> handleUpdate();
                    case 3 -> handleDelete();
                    case 4 -> selectDatabase();
                    case 5 -> selectCollection();
                    case 6 -> selectDocument();
                    case 7 -> anotherQuery = false;
                    default -> sendMessage("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage("An error occurred. Please try again.");
        }
    }


    private void handleCreate() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        String documentName = receiver.readLine();
        CRUDHandler crudServices = new CRUDHandler(databaseName, collectionName, documentName);
        System.out.println("Insert Handler");
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        queryName.add(documentName);
        try {
            JsonNode jsonNode= crudServices.createData(sender,receiver);
            BroadcastSender.broadcastNewDoc(jsonNode,queryName,Query.CreateData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Insertion done.");
    }

    private void handleUpdate() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        String documentName = receiver.readLine();
        CRUDHandler crudServices = new CRUDHandler(databaseName, collectionName, documentName);
        sendMessage("Enter the Key and value you need to update");
        System.out.println("Update Handler");
            String key = receiver.readLine();
            String value = receiver.readLine();
            crudServices.updateData(key , value);
        System.out.println("Update done.");
    }

    private void handleDelete() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        String documentName = receiver.readLine();
        CRUDHandler crudServices = new CRUDHandler(databaseName, collectionName, documentName);

        sendMessage("Enter the Key you need to delete");
        System.out.println("delete Handler");
        String key = receiver.readLine();
        crudServices.deleteData(key);
        sendMessage("Delete done.");
    }
    private void selectDatabase() throws IOException{
        String databaseName = receiver.readLine();
        System.out.println("Select "+databaseName+" database");
        sendObject.writeObject(DatabaseHandler.getDatabase(databaseName).get().toString());
        sendObject.flush();
        System.out.println("Sent!");
    }
    private void selectCollection() throws IOException{
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        sendObject.writeObject(DatabaseHandler.getDatabase(databaseName).get().getCollection(collectionName).toString());
        sendObject.flush();
    }
    private void selectDocument() throws IOException{
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        String documentName = receiver.readLine();
        String cacheKey = databaseName + "_" + collectionName + "_" + documentName;
        String documentData = (String) cache.get(cacheKey);
        if (documentData == null) {
            documentData = DatabaseHandler.getDatabase(databaseName).get()
                    .getCollection(collectionName).getDocument(documentName).toString();
            cache.put(cacheKey, documentData);
        }

        sendObject.writeObject(documentData);
        sendObject.flush();
        System.out.println("Document data sent!");
    }

    private void sendMessage(String message) {
        sender.println(message);
        sender.flush();
    }
}
