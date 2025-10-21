package org.project.storage.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.project.storage.directoryOpration.DirectoryHandler;
import org.project.storage.indexing.HashIndexer;
import org.project.storage.models.Collection;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class DocumentHandler {
    public static JsonNode createDocument(PrintWriter sender, DataInputStream receiver, String databaseName, String collectionName, String documentName) throws IOException {
        File file = getFileForPath("Database"+File.separator+databaseName, collectionName, documentName+".json");
        Collection collection = getCollection(databaseName,collectionName);
        if(collection != null) {
            collection.addDocument(documentName);
            return collection.getDocument(documentName).addDocument(sender, receiver,file);
        } else {
            System.err.println("Can't find the collection");
        }
        return null;
    }
    private static Collection getCollection(String databaseName, String collectionName) {
        return DatabaseHandler.getDatabase(databaseName).map(db -> db.getCollection(collectionName)).get();
    }
    public static void createDocument(JsonNode jsonNode, String databaseName, String collectionName, String documentName) throws IOException {
        Collection collection = getCollection(databaseName,collectionName);
            collection.addDocument(documentName);
            collection.getDocument(documentName).setJsonNode((ObjectNode) jsonNode);
    }
    public static void readDocument(String databaseName, String collectionName, String documentName) throws IOException {
        File file = getFileForPath("Database"+File.separator+databaseName, collectionName, documentName+".json");
        Collection collection = getCollection(databaseName,collectionName);
        if(collection != null) {
            collection.addDocument(documentName);
            collection.getDocument(documentName).readDataFromFile(file);
        } else {
            System.err.println("Can't find the collection");
        }
    }
    public static void deleteDocument(String databaseName, String collectionName, String documentName) {
        HashIndexer.getInstance().deleteDocument(DirectoryHandler.getDocument(databaseName, collectionName, documentName));
        DatabaseHandler.getDatabase(databaseName).get().getCollection(collectionName).deleteDocument(documentName);
        DirectoryHandler.deleteDirectory("Database"+File.separator+databaseName + File.separator
                + collectionName + File.separator
                + documentName + ".json");
    }
    public static File getFileForPath(String databaseName, String collectionName, String document) {
        return new File(databaseName + File.separator + collectionName + File.separator + document);
    }
}
