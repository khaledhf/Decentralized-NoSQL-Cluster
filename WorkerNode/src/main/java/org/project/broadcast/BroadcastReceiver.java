package org.project.broadcast;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.project.configuration.DBConfig;
import org.project.configuration.Query;
import org.project.locks.Lock;
import org.project.network.Node;
import org.project.storage.handlers.CollectionHandler;
import org.project.storage.handlers.DatabaseHandler;
import org.project.storage.handlers.DocumentHandler;
import org.project.storage.indexing.HashIndexer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class BroadcastReceiver implements Runnable{
    private ServerSocket broadcastRequest;
    private List<String> pathNames;
    private ObjectMapper mapper = new ObjectMapper();
    private DataInputStream receiver;
    private  PrintWriter sender;
    private ObjectInputStream objectReceiver;


    public BroadcastReceiver(ServerSocket broadcastRequest) {
        this.broadcastRequest = broadcastRequest;
    }

    @Override
    public void run() {
        while (true){
        try {
            Socket acceptedBroadcast = broadcastRequest.accept();
            this.receiver
                    = new DataInputStream(acceptedBroadcast.getInputStream());
            this.sender
                    = new PrintWriter(acceptedBroadcast.getOutputStream());
            this.objectReceiver
                    = new ObjectInputStream(acceptedBroadcast.getInputStream());

            Query query = Query.valueOf(receiver.readLine());
            System.out.println(query);
            pathNames = (List<String>) objectReceiver.readObject();
            switch (query) {
                case UpdateData -> updateData();
                case DeleteData -> deleteData();
                case CreateData -> createData();
                case DeleteAllData -> deleteAllData();
                case DeleteCollection -> CollectionHandler.deleteCollection(this.pathNames.get(0), this.pathNames.get(1));
                case DeleteDatabase -> DatabaseHandler.deleteDatabase(this.pathNames.get(0));
                case AddDocument -> DocumentHandler.readDocument(this.pathNames.get(0),
                        this.pathNames.get(1), this.pathNames.get(2));
                case CreateCollection -> CollectionHandler.createCollection(this.pathNames.get(0),
                        this.pathNames.get(1));
                case CreateDatabase -> DatabaseHandler.createDatabase(this.pathNames.get(0));
            }
            sender.println("Node's "+ Node.getNodeId()+" broadcast has done successfully");
            sender.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        }
    }

    private void updateData(){
        System.out.println("Updating Data from broadcasting");
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(DatabaseHandler.getDatabase(pathNames.get(0)).get()
                    .getCollection(pathNames.get(1))
                    .getDocument(pathNames.get(2)).getFile());
            Lock.getInstance().getLock(jsonNode.get("id").asText()).writeLock().lock();
            DatabaseHandler.getDatabase(pathNames.get(0)).get()
                    .getCollection(pathNames.get(1))
                    .getDocument(pathNames.get(2))
                    .replaceField(pathNames.get(3),pathNames.get(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Lock.getInstance().getLock(jsonNode.get("id").asText()).writeLock().unlock();
            System.out.println("Finish update Data");

        }
    }
    private void createData(){
        try {
            System.out.println("Creating Data from broadcasting");
            JsonNode jsonNode = (JsonNode) objectReceiver.readObject();
            System.out.println(jsonNode);
            System.out.println(jsonNode.get("id").asText());
            DocumentHandler.createDocument(jsonNode,pathNames.get(0),pathNames.get(1),pathNames.get(0));
            ObjectWriter objectWriter = mapper.writer(new DefaultPrettyPrinter());

            objectWriter.writeValue(new File(DBConfig.BASE_DIRECTORY+File.separator+pathNames.get(0)+File.separator+pathNames.get(1)+File.separator+pathNames.get(2)+".json"),
                    jsonNode);
            HashIndexer.getInstance().add((ObjectNode) jsonNode);
            System.out.println("adding Document has done successfully!");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteData(){
        System.out.println("Deleting Data from broadcasting");
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(DatabaseHandler.getDatabase(pathNames.get(0)).get()
                    .getCollection(pathNames.get(1))
                    .getDocument(pathNames.get(2)).getFile());
            Lock.getInstance().getLock(jsonNode.get("id").asText()).writeLock().lock();
            DatabaseHandler.getDatabase(pathNames.get(0)).get()
                    .getCollection(pathNames.get(1))
                    .getDocument(pathNames.get(2))
                    .removeField(pathNames.get(3));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Lock.getInstance().getLock(jsonNode.get("id").asText()).writeLock().unlock();
            System.out.println("Finish Delete Data");
        }
    }
    private void deleteAllData(){
        System.out.println("Deleting All Data from broadcasting");
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(DatabaseHandler.getDatabase(pathNames.get(0)).get()
                    .getCollection(pathNames.get(1))
                    .getDocument(pathNames.get(2)).getFile());
            Lock.getInstance().getLock(jsonNode.get("id").asText()).writeLock().lock();
            DocumentHandler.deleteDocument(pathNames.get(0),pathNames.get(1),
                    pathNames.get(2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Lock.getInstance().getLock(jsonNode.get("id").asText()).writeLock().unlock();
            System.out.println("Finish Delete all");
        }
    }

}
