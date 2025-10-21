package org.project;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.project.broadcast.BroadcastReceiver;
import org.project.configuration.AffinityPortsCalculator;
import org.project.configuration.Query;
import org.project.network.BootstrapCommunicator;
import org.project.network.Node;
import org.project.network.NodeListener;
import org.project.network.UserListener;
import org.project.storage.directoryOpration.DatabaseLoader;
import org.project.storage.directoryOpration.DirectoryHandler;
import org.project.storage.indexing.HashIndexer;
import org.project.validation.UserValidator;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        int basePort = Integer.parseInt(args[0]);
        int nodeID = Integer.parseInt(args[1]);
        Node.getInstance(nodeID, basePort);
        System.out.println("Node Number "+nodeID+" has been initialized with base port "+basePort);
        ServerSocket userListener = new ServerSocket(basePort);
        ServerSocket nodeListener = new ServerSocket(basePort + 100);
        ServerSocket bootstrapListener = new ServerSocket(basePort +200);
        ServerSocket broadcastListener = new ServerSocket(basePort +300 );

        DatabaseLoader.databaseLoader();
       // System.out.println(AffinityPortsCalculator.getAffinityPort(1));
        //System.out.println(AffinityPortsCalculator.getAffinityPort(2));
        NodeListener nodeListenerThread = new NodeListener(nodeListener);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(broadcastListener);
        new Thread(nodeListenerThread).start();
        new Thread(broadcastReceiver).start();

        BootstrapCommunicator bootstrapCommunicator = new BootstrapCommunicator(bootstrapListener);
        new Thread(bootstrapCommunicator).start();
        System.out.println("Every Setting is Ready To Receive Users :)");
        while (true){
            Socket user = userListener.accept();
            UserListener userListenerThread = new UserListener(user);
            new Thread(userListenerThread).start();

        }


    }
}