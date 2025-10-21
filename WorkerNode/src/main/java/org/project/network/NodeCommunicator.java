package org.project.network;

import org.project.configuration.Query;
import org.project.storage.handlers.CRUDHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class NodeCommunicator{
    private static PrintWriter sender;
    private static ObjectOutputStream sendObject;


    public static void sendToAffinityNode(Query query, int port,  List<String> queryName){
        try {
            Socket affinityNode =
                    new Socket(InetAddress.getLocalHost(), port);
            sender = new PrintWriter(affinityNode.getOutputStream());
            sendObject = new ObjectOutputStream(affinityNode.getOutputStream());
            sendMessage(String.valueOf(query));
            sendObject.writeObject(queryName);
            sendObject.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void sendMessage(String message){
        sender.println(message);
        sender.flush();
    }
}
