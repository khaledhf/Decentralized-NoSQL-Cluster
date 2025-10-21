package org.project.storage.directoryOpration;

import org.project.configuration.DBConfig;
import org.project.storage.handlers.CollectionHandler;
import org.project.storage.handlers.DatabaseHandler;
import org.project.storage.handlers.DocumentHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class DatabaseLoader {
    public static void databaseLoader() throws IOException {
        File rootDirectory = Paths.get(DBConfig.BASE_DIRECTORY + File.separator).toFile();
        for (File database : rootDirectory.listFiles()) {
            if (database.isDirectory()) {
                DatabaseHandler.createDatabase(database.getName());
                loadCollections(database);
            }
        }
    }
    private static void loadCollections(File database) throws IOException {
        File[] listOfCollections = database.listFiles();
        for (File collection : listOfCollections) {
            if (collection.isDirectory()) {
                CollectionHandler.createCollection(database.getName(), collection.getName());
                loadDocuments(database.getName(), collection);
            }
        }
    }
    private static void loadDocuments(String databaseName, File collection) throws IOException {
        File[] listOfDocuments = collection.listFiles();
        for (File document : listOfDocuments) {
            if (document.isFile()) {
                String documentName = extractFileNameWithoutExtension(document);
                DocumentHandler.readDocument(databaseName, collection.getName(), documentName);
            }
        }
    }
    public static String extractFileNameWithoutExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex > 0) ? fileName.substring(0, lastDotIndex) : fileName;
    }
}
