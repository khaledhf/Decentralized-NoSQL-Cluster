package org.project.storage.handlers;

import org.project.storage.directoryOpration.DirectoryHandler;
import org.project.storage.indexing.HashIndexer;

import java.io.File;
import java.io.IOException;

public class CollectionHandler {

    public static void createCollection(String databaseName, String collectionName) {
        DatabaseHandler.getDatabase(databaseName).get().addCollection(collectionName);
        DirectoryHandler.createDirectory(databaseName + "/" + collectionName);
    }
    public static void deleteCollection(String databaseName, String collectionName) throws IOException {
        DirectoryHandler.getDocuments(databaseName, collectionName);
        HashIndexer.getInstance().deleteDocuments(DirectoryHandler.getDocuments(databaseName, collectionName));
        DatabaseHandler.getDatabase(databaseName).get()
                .deleteCollection(collectionName);
        DirectoryHandler.deleteDirectory(databaseName + File.separator + collectionName);
    }
}
