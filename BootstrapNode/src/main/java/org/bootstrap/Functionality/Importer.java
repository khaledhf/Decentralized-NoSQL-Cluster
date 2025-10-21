package org.bootstrap.Functionality;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class Importer {
    public static void importUsernames(){
        try {
            new FileWriter("UsersDetails.txt", false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int port: NodePorts.getNodesPorts()) {
            try {
                System.out.println(port);
                Socket nodeSocket = new Socket(InetAddress.getLocalHost(),port);
                PrintWriter sender
                        = new PrintWriter(nodeSocket.getOutputStream());
                ObjectInputStream receiveObject
                        = new ObjectInputStream(nodeSocket.getInputStream());
                sender.println(BootstrapQuery.ImportUsernames);
                sender.flush();
                List<String> userDetails = (List<String>) receiveObject.readObject();
                saveUserDetails(userDetails);

                sender.close();
                receiveObject.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void saveUserDetails(List<String> userDetails) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("UsersDetails.txt", true))) {
            for (String detail : userDetails) {
                writer.println(detail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
