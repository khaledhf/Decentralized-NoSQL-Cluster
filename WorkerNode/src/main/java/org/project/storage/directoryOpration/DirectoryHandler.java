package org.project.storage.directoryOpration;

import org.project.configuration.DBConfig;
import org.project.storage.handlers.DatabaseHandler;
import org.project.storage.models.Document;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DirectoryHandler {

    public static void createDirectory(String path){
        File dirs = new File(DBConfig.BASE_DIRECTORY + File.separator+path);
        dirs.mkdir();
    }
    public static void deleteDirectory(String databasePath) {
        File dir =new File(databasePath);
        System.out.println(dir);
        try {
            FileUtils.forceDelete(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Document> getDocuments(String databaseName) {
        ArrayList<Document> documents = new ArrayList<>();
        File databaseFolder = new File(DBConfig.BASE_DIRECTORY
                + File.separator + databaseName
                + File.separator);

        if (!databaseFolder.exists()) {
            return documents;
        }

        File[] listOfCollections = databaseFolder.listFiles();
        for (File collection : listOfCollections) {
            if (collection.isDirectory()) {
                documents.addAll(getDocuments(databaseName, collection.getName()));
            }
        }
        return documents;
    }

    public static ArrayList<Document> getDocuments(String databaseName
            , String collectionName) {
        ArrayList<Document> documents = new ArrayList<>();
        File collectionFolder =
                new File(DBConfig.BASE_DIRECTORY
                        + File.separator + databaseName
                        + File.separator + collectionName);

        if (!collectionFolder.exists()) {
            System.err.println("Collection is not Exist!!");
            return documents;
        }
        File[] listOfDocuments = collectionFolder.listFiles();
        for (File documentFile : listOfDocuments) {
            if (documentFile.isFile()) {
                String documentName = DatabaseLoader.extractFileNameWithoutExtension(documentFile);
                Document document = DatabaseHandler.getDatabase(databaseName).get()
                        .getCollection(collectionName)
                        .getDocument(documentName);
                if (document != null) {
                    documents.add(document);
                }
            }
        }
        return documents;
    }
    public static Document getDocument(String databaseName, String collectionName, String documentName)  {
        File documentFile = new File(DBConfig.BASE_DIRECTORY + File.separator +
                databaseName + File.separator +
                collectionName+ File.separator
                + documentName + DBConfig.FILE_EXTENSION);
        if (documentFile.isFile()) {
            return DatabaseHandler.getDatabase(databaseName).get()
                    .getCollection(collectionName).getDocument(documentName);
        }
        return  null;
    }
    public static File openFile(String databaseName, String collectionName, String documentName) {
        String filePath = DBConfig.BASE_DIRECTORY + File.separator +
                databaseName + File.separator +
                collectionName + File.separator +
                documentName + DBConfig.FILE_EXTENSION;

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Error creating file: " + filePath, e);
            }
        }
        return file;
    }
}
