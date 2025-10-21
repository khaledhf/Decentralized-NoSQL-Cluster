package org.project.network;

import org.project.configuration.BootstrapQuery;
import org.project.configuration.Query;
import org.project.validation.UserValidator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BootstrapCommunicator implements Runnable {

    private ServerSocket bootstrapSocket;

    public BootstrapCommunicator(ServerSocket bootstrapSocket) {
        this.bootstrapSocket = bootstrapSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket bootstrapSocket = this.bootstrapSocket.accept();
                BufferedReader receiver
                        = new BufferedReader(new InputStreamReader(bootstrapSocket.getInputStream()));
                ObjectOutputStream sendObject
                        = new ObjectOutputStream(bootstrapSocket.getOutputStream());
                BootstrapQuery action = BootstrapQuery.valueOf(receiver.readLine());
                switch (action) {
                    case ImportUsernames -> sendUsernameToBootstrap(sendObject);
                    case ExportUserDetails -> receiveUserDetailsFromBootstrap(receiver);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendUsernameToBootstrap(ObjectOutputStream sendObject) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("Users/Node" + Node.getNodeId() + "_Users.txt"));
        List<String> out = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            String username = parts[0];
            String role = parts[2];
            out.add(username + ":" + role + ":" + Node.getNodeId());
        }
        sendObject.writeObject(out);
        sendObject.flush();
    }
    public void receiveUserDetailsFromBootstrap(BufferedReader receiver) throws IOException {
        String username = receiver.readLine();
        String password = receiver.readLine();
        String role = receiver.readLine();
        System.out.println(username);
        System.out.println(password);
        System.out.println(role);
        new UserValidator().addToFile(username, password, role);
    }
}


