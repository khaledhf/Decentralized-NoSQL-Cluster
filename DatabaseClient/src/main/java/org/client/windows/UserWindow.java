package org.client.windows;

import org.client.configuration.Query;
import org.client.configuration.Utility;

import java.io.*;
import java.util.Scanner;

public class UserWindow implements Window{
    private BufferedReader receiver;

    private PrintWriter sender;
    static ObjectInputStream receiveObject;

    Scanner input = new Scanner(System.in);

    public UserWindow(BufferedReader receiver, PrintWriter sender, ObjectInputStream receiveObject) {
        this.receiver = receiver;
        this.sender = sender;
        this.receiveObject = receiveObject;
    }

    @Override
    public void popupWindow() {
        boolean hasElse = true;
        while (hasElse) {
            try {
                System.out.println("Enter what query you want:");
                System.out.println("1. " + Query.CreateData);
                System.out.println("2. " + Query.UpdateData);
                System.out.println("3. " + Query.DeleteData);
                System.out.println("4. " + Query.ListDatabase);
                System.out.println("5. " + Query.ListCollection);
                System.out.println("6. " + Query.ListDocument);
                System.out.println("7. Exit");
                int queryChoice = input.nextInt();
                System.out.println(queryChoice);
                Utility.sendMessage(sender, String.valueOf(queryChoice));
                switch (queryChoice) {
                    case 1 -> handleCreate();
                    case 2 -> handleUpdate();
                    case 3 -> handleDelete();
                    case 4 -> selectDatabase();
                    case 5 -> selectCollection();
                    case 6 -> selectDocument();
                    case 7 -> hasElse = false;
                    default -> System.out.println("We have only 7 Choices :'(");
                }

            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void handleCreate() throws IOException {
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

        String key = input.nextLine();
        String value = input.nextLine();
        Utility.sendMessage(sender, key);
        Utility.sendMessage(sender, value);

        System.out.println("Finish updating data to document : "+documentName+
                "\nwhich Key: "+key+" have new value: "+value);

    }

    private void handleDelete() throws IOException {
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

        String key = input.nextLine();
        Utility.sendMessage(sender, key);
        System.out.println("Finish deleting key: "+key+" from document : " +documentName);
    }
    private void selectDatabase() throws IOException, ClassNotFoundException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println(receiveObject.readObject());

    }
    private void selectCollection() throws IOException, ClassNotFoundException {
        input.nextLine();
        System.out.println("Enter the database name:");
        String databaseName = input.nextLine();
        Utility.sendMessage(sender, databaseName);
        System.out.println("Enter the collection name:");
        String collectionName = input.nextLine();
        Utility.sendMessage(sender, collectionName);
        System.out.println(receiveObject.readObject());

    }
    private void selectDocument() throws IOException, ClassNotFoundException {
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
        System.out.println(receiveObject.readObject());
    }
}
