package org.client.windows;

import org.client.configuration.Query;
import org.client.configuration.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AdminWindow implements Window{
    private BufferedReader receiver;

    private PrintWriter sender;
    Scanner input = new Scanner(System.in);



    public AdminWindow(BufferedReader receiver, PrintWriter sender) {
        this.receiver = receiver;
        this.sender = sender;
    }
    @Override
    public void popupWindow() {
        boolean hasElse = true;
        while (hasElse) {
            try {
        System.out.println("Enter your choice:");
        System.out.println("1. "+ Query.CreateData);
        System.out.println("2. "+Query.UpdateData);
        System.out.println("3. "+Query.DeleteData);
        System.out.println("4. "+Query.CreateDatabase);
        System.out.println("5. "+Query.CreateCollection);
        System.out.println("6. "+Query.AddDocument);
        System.out.println("7. "+Query.DeleteAllData);
        System.out.println("8. "+Query.DeleteCollection);
        System.out.println("9. "+Query.DeleteDatabase);
        System.out.println("10. Exit");
        int queryChoice = input.nextInt();
        Utility.sendMessage(sender, String.valueOf(queryChoice));
        switch (queryChoice) {
            case 1 -> handleCreateData();
            case 2 -> handleUpdate();
            case 3 -> handleDeleteData();
            case 4 -> handleCreateDatabase();
            case 5 -> handleCreateCollection();
            case 6 -> handleAddDocument();
            case 7 -> handleDeleteAllData();
            case 8 -> handleDeleteCollection();
            case 9 -> handleDeleteDatabase();
            case 10 -> hasElse = false;
            default -> System.out.println("We have only 10 Choices :'(");
        }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void handleCreateData() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);
        System.out.println("Enter the document name:");
        String documentName = input.nextLine();
        Utility.sendMessage(sender, documentName);

        System.out.println(receiver.readLine());

        String attribute;
        String value;
        String newAttribute = input.nextLine();
        do {
            attribute = newAttribute;
            value = input.nextLine();
            Utility.sendMessage(sender, attribute);
            Utility.sendMessage(sender, value);
            newAttribute = input.nextLine();
        }while (!newAttribute.equalsIgnoreCase("exit"));
        Utility.sendMessage(sender, newAttribute);
        System.out.println("Finish adding data to document : "+documentName);

    }
    private void handleUpdate() throws IOException {
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);
        System.out.println("Enter the document name:");
        String documentName = input.nextLine();
        Utility.sendMessage(sender, documentName);

        System.out.println(receiver.readLine());

        String key = input.nextLine();
        String value = input.nextLine();
        Utility.sendMessage(sender, key);
        Utility.sendMessage(sender, value);

        System.out.println("Finish updating data to document : "+documentName+
                "\nwhich Key: "+key+" have new value: "+value);

    }
    private void handleDeleteData() throws IOException {
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);
        System.out.println("Enter the document name:");
        String documentName = input.nextLine();
        Utility.sendMessage(sender, documentName);

        System.out.println(receiver.readLine());

        String key = input.nextLine();
        Utility.sendMessage(sender, key);
        System.out.println("Finish deleting key: "+key+" from document : " +documentName);
    }
    private void handleCreateDatabase() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);

        System.out.println(receiver.readLine());
        System.out.println("Creation " + databaseName+" Done successfully");
    }
    private void handleCreateCollection() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);

        System.out.println(receiver.readLine());
        System.out.println("Creation " + collectionName+" Done successfully");

    }
    private void handleAddDocument() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);
        System.out.println("Enter the document name:");
        String documentName = input.nextLine();
        Utility.sendMessage(sender, documentName);

        System.out.println(receiver.readLine());
        System.out.println("Creation " + documentName+" Done successfully");

    }
    private void handleDeleteAllData() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);
        System.out.println("Enter the document name:");
        String documentName = input.nextLine();
        Utility.sendMessage(sender, documentName);

        System.out.println(receiver.readLine());

        System.out.println("Delete " + documentName+" Done successfully");
    }
    private void handleDeleteDatabase() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println(receiver.readLine());
        System.out.println("delete " + databaseName+" Done successfully");
    }
    private void handleDeleteCollection() throws IOException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);

        System.out.println(receiver.readLine());
        System.out.println("Delete " + collectionName+" Done successfully");
    }
}
