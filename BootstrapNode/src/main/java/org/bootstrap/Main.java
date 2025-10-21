package org.bootstrap;

import org.bootstrap.Balancer.LoadBalancer;
import org.bootstrap.Functionality.FileConfig;
import org.bootstrap.Functionality.Importer;
import org.bootstrap.Functionality.NodePorts;
import org.bootstrap.Functionality.RunNodes;
import org.bootstrap.UsersHandler.UserListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        RunNodes.run();
        try {
            System.out.println("Waiting for containers start...!");
            Thread.sleep(1);
            List<Integer> nodes=new ArrayList<>();
            for (int i = 0 ; i < FileConfig.NODES_NUMBER ; i++)
                nodes.add((i+1));
            LoadBalancer.getInstance().setNodes(nodes);
            for (int nodeID: nodes) {
                System.out.println("Node_"+nodeID+" bind to port "+NodePorts.getNodePort(nodeID));
            }
            ServerSocket serverSocket=new ServerSocket(1234);
            Importer.importUsernames();
            while (true){
                Socket userConnection = serverSocket.accept();
                UserListener userHandler= new UserListener(userConnection);
                new Thread(userHandler).start();
            }

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}