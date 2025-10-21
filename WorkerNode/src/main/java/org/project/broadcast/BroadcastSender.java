package org.project.broadcast;

import com.fasterxml.jackson.databind.JsonNode;
import org.project.configuration.Query;
import org.project.network.Node;
import org.project.network.NodeConnection;
import org.project.storage.handlers.CRUDHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

public class BroadcastSender {
    public static void broadcastMessage(List<String> queryName, Query query) {
        for (int nodePort : Node.getListenerPorts()) {
            try (NodeConnection connection = new NodeConnection(nodePort + 300)) {
                connection.sendMessage(query.toString());
                connection.sendObject(queryName);
                System.out.println(connection.receiveMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void broadcastNewDoc(JsonNode jsonFile, List<String> queryName, Query query) {
        for (int nodePort : Node.getListenerPorts()) {
            try (NodeConnection connection = new NodeConnection(nodePort + 300)) {
                connection.sendMessage(query.toString());
                connection.sendObject(queryName);
                connection.sendObject(jsonFile);
                System.out.println(connection.receiveMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}