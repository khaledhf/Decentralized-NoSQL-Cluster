package org.project.storage.handlers;

import org.project.storage.directoryOpration.DirectoryHandler;
import org.project.storage.indexing.HashIndexer;
import org.project.storage.models.Database;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseHandler {
    private static final CopyOnWriteArrayList<Database> databaseList = new CopyOnWriteArrayList<>();  // thread-safe list

    public static Optional<Database> getDatabase(String name) {
        return databaseList.stream().filter(database -> database.getDatabaseName().equals(name)).findFirst();
    }
    public static void createDatabase(String database) {
        if(getDatabase(database).isPresent()) {
            System.out.println("The Database is exist");
        }
        databaseList.add(new Database(database));
        DirectoryHandler.createDirectory(database);
    }

  public static void deleteDatabase(String databaseName){
      DirectoryHandler.getDocuments(databaseName);
      HashIndexer.getInstance().deleteDocuments(DirectoryHandler.getDocuments(databaseName));
        databaseList.removeIf(database -> database.getDatabaseName().equals(databaseName));
        DirectoryHandler.deleteDirectory(databaseName);
    }

}
