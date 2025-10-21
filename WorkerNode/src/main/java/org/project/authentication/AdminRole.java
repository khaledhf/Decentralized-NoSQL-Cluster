package org.project.authentication;

import com.fasterxml.jackson.databind.JsonNode;
import org.project.broadcast.BroadcastSender;
import org.project.configuration.Query;
import org.project.storage.handlers.CRUDHandler;
import org.project.storage.handlers.CollectionHandler;
import org.project.storage.handlers.DatabaseHandler;
import org.project.storage.handlers.DocumentHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdminRole implements Roles{
    private DataInputStream receiver;
    private PrintWriter sender;

    public AdminRole(DataInputStream receiver, PrintWriter sender) {
        this.receiver = receiver;
        this.sender = sender;
    }
    @Override
    public void start() {
    boolean continueInteraction = true;
            try {
                while (continueInteraction) {
                    int choice = Integer.parseInt(receiver.readLine());
                    switch (choice) {
                        case 1 -> handleCreateData();
                        case 2 -> handleUpdate();
                        case 3 -> handleDeleteData();
                        case 4 -> handleCreateDatabase();
                        case 5 -> handleCreateCollection();
                        case 6 -> handleAddDocument();
                        case 7 -> handleDeleteAllData();
                        case 8 -> handleDeleteCollection();
                        case 9 -> handleDeleteDatabase();
                        case 10 -> continueInteraction = false;
                        default -> sendMessage("Invalid choice. Please try again.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendMessage("An error occurred. Please try again.");
            }


}

    private void handleCreateData() throws IOException {
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

    private void handleDeleteData() throws IOException {
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

    private void handleCreateDatabase() throws IOException {
        String databaseName = receiver.readLine();
        DatabaseHandler.createDatabase(databaseName);
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        BroadcastSender.broadcastMessage(queryName,Query.CreateDatabase);
        System.out.println("Create Database Handler");
        sendMessage("Creation done.");
    }
    private void handleCreateCollection() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        CollectionHandler.createCollection(databaseName,collectionName);
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        BroadcastSender.broadcastMessage(queryName,Query.CreateCollection);
        System.out.println("Create Collection Handler");
        sendMessage("Creation done.");
    }
    private void handleAddDocument() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        String documentName = receiver.readLine();
        DocumentHandler.readDocument(databaseName, collectionName, documentName);
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        queryName.add(documentName);
        BroadcastSender.broadcastMessage(queryName,Query.AddDocument);
        System.out.println("Create Document Handler");
        sendMessage("Creation done.");
    }
    private void handleDeleteAllData() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        String documentName = receiver.readLine();
        CRUDHandler crudServices = new CRUDHandler(databaseName, collectionName, documentName);
        crudServices.deleteAllData();
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        queryName.add(documentName);
        BroadcastSender.broadcastMessage(queryName,Query.DeleteAllData);
        System.out.println("Delete Document Handler");
        sendMessage("Delete done.");
    }
    private void handleDeleteDatabase() throws IOException {
        String databaseName = receiver.readLine();
        DatabaseHandler.deleteDatabase(databaseName);
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        BroadcastSender.broadcastMessage(queryName,Query.DeleteDatabase);
        System.out.println("Create Database Handler");
        sendMessage("Creation done.");
    }
    private void handleDeleteCollection() throws IOException {
        String databaseName = receiver.readLine();
        String collectionName = receiver.readLine();
        CollectionHandler.deleteCollection(databaseName,collectionName);
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        BroadcastSender.broadcastMessage(queryName,Query.DeleteCollection);
        System.out.println("Create Collection Handler");
        sendMessage("Creation done.");
    }
    private void sendMessage(String message) {
        sender.println(message);
        sender.flush();
    }
}
