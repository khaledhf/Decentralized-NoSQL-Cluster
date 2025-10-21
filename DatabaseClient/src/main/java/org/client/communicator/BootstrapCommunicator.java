package org.client.communicator;

import org.client.configuration.BootstrapQuery;
import org.client.configuration.PortConfig;
import org.client.configuration.Utility;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class BootstrapCommunicator {
    private static BootstrapCommunicator instance;
    private Socket bootstrap;
    private BufferedReader receiver ;
    private PrintWriter sender;
    private BootstrapCommunicator() {
        try {

            this.bootstrap
                    = new Socket(InetAddress.getLocalHost(), PortConfig.BOOTSTRAP_PORT);
            this.receiver
                    = new BufferedReader(new InputStreamReader(bootstrap.getInputStream()));
            this.sender
                    = new PrintWriter(bootstrap.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static synchronized BootstrapCommunicator getInstance(){
        if(instance == null){
            synchronized (BootstrapCommunicator.class) {
            instance =  new BootstrapCommunicator();
        }
        }
        return instance;
    }

    public int getNodeID(String username) throws IOException {
        sender.println(BootstrapQuery.Login);
        sender.flush();
        sender.println(username);
        sender.flush();
        return Integer.parseInt(receiver.readLine());
    }
    public boolean usernameValidator(String username) throws IOException {
        sender.println(BootstrapQuery.Signup);
        sender.flush();
        sender.println(username);
        sender.flush();
        return Boolean.parseBoolean(receiver.readLine());
    }
    public void signupNewUser(String username, String password, String role){
        Utility.sendMessage(sender,password);
        Utility.sendMessage(sender,role);
    }

}
