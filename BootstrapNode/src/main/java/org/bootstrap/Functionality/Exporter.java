package org.bootstrap.Functionality;

import org.bootstrap.Balancer.LoadBalancer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Exporter {
    public static void exportUser(String username, String password, String role){
        try {

        int nodeID = LoadBalancer.getInstance().getNextNode();
            System.out.println(nodeID);
            Socket node = new Socket(InetAddress.getLocalHost(),NodePorts.getNodePort(nodeID));
            PrintWriter sender =  new PrintWriter(node.getOutputStream());
            sender.println(BootstrapQuery.ExportUserDetails);
            sender.flush();
            Utility.sendMessage(sender,username);
            Utility.sendMessage(sender,password);
            Utility.sendMessage(sender,role);
            PrintWriter writer = new PrintWriter(new FileWriter("UsersDetails.txt", true));
            writer.write(username+":"+role+":"+nodeID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
