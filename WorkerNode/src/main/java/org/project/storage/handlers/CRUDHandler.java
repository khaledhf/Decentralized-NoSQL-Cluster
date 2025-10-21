package org.project.storage.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import org.project.broadcast.BroadcastSender;
import org.project.cache.CacheManager;
import org.project.cache.LRUCache;
import org.project.configuration.AffinityPortsCalculator;
import org.project.configuration.Query;
import org.project.locks.Lock;
import org.project.network.Node;
import org.project.network.NodeCommunicator;
import org.project.storage.directoryOpration.DatabaseLoader;
import org.project.storage.directoryOpration.DirectoryHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDHandler implements Serializable {
    private String databaseName;
    private String collectionName;
    private String documentName;
    private File file;
    private String attribute;
    private String value;
    private LRUCache<String, Object> cache = CacheManager.getInstance();


    public CRUDHandler(String databaseName, String collectionName, String documentName) {
        this.databaseName = databaseName;
        this.collectionName = collectionName;
        this.documentName = documentName;
        this.file = DirectoryHandler.openFile(databaseName, collectionName, documentName);
    }
    public JsonNode createData(PrintWriter sender, DataInputStream receiver) throws IOException {
        JsonNode jsonNode = DocumentHandler.createDocument(sender,receiver,databaseName,collectionName,documentName);
        String key = databaseName + "_" + collectionName + "_" + documentName;
        cache.put(key, jsonNode);
        DatabaseLoader.databaseLoader();
        return jsonNode;
    }

    public void updateData(String attribute, String value){
        this.attribute = attribute;
        this.value = value;
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        queryName.add(documentName);
        queryName.add(attribute);
        queryName.add(value);
        if(checkAffinity()){
            String id = DatabaseHandler.getDatabase(databaseName).get()
                    .getCollection(collectionName)
                    .getDocument(documentName)
                    .getId();
            try {
                Lock.getInstance().getLock(id).writeLock().lock();
                DatabaseHandler.getDatabase(databaseName).get()
                        .getCollection(collectionName)
                        .getDocument(documentName)
                        .replaceField(attribute, value);
                String key = databaseName + "_" + collectionName + "_" + documentName;
                JsonNode updatedData = DatabaseHandler.getDatabase(databaseName).get()
                        .getCollection(collectionName)
                        .getDocument(documentName).getData();
                cache.put(key, updatedData);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                Lock.getInstance().getLock(id).writeLock().unlock();

            }
            BroadcastSender.broadcastMessage(queryName,Query.UpdateData);
        }
        else{
            int affinityNode= getNodeAffinity();
            NodeCommunicator.sendToAffinityNode(Query.UpdateData, AffinityPortsCalculator.getAffinityPort(affinityNode),queryName);
        }
    }


    public void deleteData(String attribute){
        this.attribute = attribute;
        List<String> queryName= new ArrayList<>();
        queryName.add(databaseName);
        queryName.add(collectionName);
        queryName.add(documentName);
        queryName.add(attribute);
        if(checkAffinity()){
            String id = DatabaseHandler.getDatabase(databaseName).get()
                    .getCollection(collectionName)
                    .getDocument(documentName)
                    .getId();
            try {
                Lock.getInstance().getLock(id).writeLock().lock();
                DatabaseHandler.getDatabase(databaseName).get()
                        .getCollection(collectionName)
                        .getDocument(documentName)
                        .removeField(attribute);
                String key = databaseName + "_" + collectionName + "_" + documentName;
                cache.remove(key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                Lock.getInstance().getLock(id).writeLock().unlock();

            }
            BroadcastSender.broadcastMessage(queryName,Query.DeleteData);
        }
        else{
            int affinityNode= getNodeAffinity();
            NodeCommunicator.sendToAffinityNode(Query.DeleteData, AffinityPortsCalculator.getAffinityPort(affinityNode),queryName);
        }
    }

    public void deleteAllData(){
        if(checkAffinity()){
            String id = DatabaseHandler.getDatabase(databaseName).get()
                    .getCollection(collectionName)
                    .getDocument(documentName)
                    .getId();
            try {
                Lock.getInstance().getLock(id).writeLock().lock();
                DocumentHandler.deleteDocument(databaseName,collectionName,documentName);
            } finally {
                Lock.getInstance().getLock(id).writeLock().unlock();

            }
        }
        else{
            int affinityNode= getNodeAffinity();
            List<String> queryName= new ArrayList<>();
            queryName.add(databaseName);
            queryName.add(collectionName);
            queryName.add(documentName);
            NodeCommunicator.sendToAffinityNode(Query.DeleteAllData, AffinityPortsCalculator.getAffinityPort(affinityNode),queryName);
        }
    }

    private boolean checkAffinity(){
        return Node.getNodeId() == DatabaseHandler.getDatabase(databaseName).get()
                .getCollection(collectionName)
                .getDocument(documentName)
                .getAffinity();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getDocumentName() {
        return documentName;
    }
    private int getNodeAffinity(){
        return DatabaseHandler.getDatabase(databaseName).get()
            .getCollection(collectionName)
            .getDocument(documentName)
            .getAffinity();}

}

