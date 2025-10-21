package org.project.network;

import org.project.configuration.DBConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public enum NodeStatus {
        INITIALIZING, ACTIVE, INACTIVE, MAINTENANCE, FAILED
    }
    private final int FIRST_PORT = 1500;
    private static int nodeId;
    private String ipAddress;
    private int port;
    private NodeStatus status;
    private static Node instance;
    private static List<Integer> listenerPorts;

    private static File usersInfo;

    public static Node getInstance(int nodeId, int port) {
        if (instance == null) {
            instance = new Node(nodeId, port);
        }
        return instance;
    }

    private Node(int nodeId,  int port) {
        this.nodeId = nodeId;
        this.port = port;
        this.listenerPorts = new ArrayList<>();
        this.status = NodeStatus.INITIALIZING;
        setListenerPorts();
        setUsersInfo(nodeId);
    }

    public static int getNodeId() {
        return nodeId;
    }
    private void setListenerPorts() {
        int firstPort = FIRST_PORT;
        for(int i = 0; i< DBConfig.NODES_NUMBER; i++){
            if (firstPort == port){
                firstPort+=1000;
                continue;}
            listenerPorts.add(firstPort);
            firstPort+=1000;

        }
    }
    private void setUsersInfo(int nodeId){
        usersInfo = Paths.get("Users/Node" + nodeId + "_Users.txt").toFile();
    }
    public static File getUsersInfo() {
        return usersInfo;
    }

    public static List<Integer> getListenerPorts() {
        return listenerPorts;
    }




}