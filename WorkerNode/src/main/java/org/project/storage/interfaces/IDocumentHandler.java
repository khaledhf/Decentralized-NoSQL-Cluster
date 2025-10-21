package org.project.storage.interfaces;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;

public interface IDocumentHandler {
    JsonNode createDocument(PrintWriter sender, DataInputStream receiver, String databaseName, String collectionName, String documentName) throws IOException;
    void createDocument(JsonNode jsonNode, String databaseName, String collectionName, String documentName) throws IOException;
    void readDocument(String databaseName, String collectionName, String documentName) throws IOException;
    void deleteDocument(String databaseName, String collectionName, String documentName);
}


