package org.bootstrap.UsersHandler;

import org.bootstrap.Functionality.BootstrapQuery;
import org.bootstrap.Functionality.Exporter;
import org.bootstrap.Functionality.Importer;
import org.bootstrap.Functionality.Utility;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UserListener implements Runnable{
    private BufferedReader receiver;

    private PrintWriter sender;
    private Socket userSocket;

    public UserListener(Socket userSocket) {
        this.userSocket = userSocket;
    }

    @Override
    public void run() {
        try {
        this.receiver  = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        this.sender =  new PrintWriter(userSocket.getOutputStream());
        BootstrapQuery action = BootstrapQuery.valueOf(receiver.readLine());
        switch (action){
            case Login -> checkForNodeID();
            case Signup -> signupNewUser();
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void checkForNodeID() throws IOException {
        String username = receiver.readLine();
        BufferedReader reader = new BufferedReader(new FileReader("UsersDetails.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if(username.equals(parts[0])){
                sender.println(parts[2]);
                sender.flush();
                sender.println(parts[1]);
                sender.flush();
                return;
            }
        }
        sender.println(-1);
        sender.flush();
    }
    private void signupNewUser() throws IOException {
        String username = receiver.readLine();
        System.out.println(username);
        BufferedReader reader = new BufferedReader(new FileReader("UsersDetails.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if(username.equals(parts[0])){
                Utility.sendMessage(sender, String.valueOf(false));
                return;
            }
            Utility.sendMessage(sender, String.valueOf(true));
            String password = receiver.readLine();
            String role = receiver.readLine();
            Exporter.exportUser(username, password, role);
            Importer.importUsernames();
        }
    }
}
