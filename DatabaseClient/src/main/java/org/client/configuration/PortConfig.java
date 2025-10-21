package org.client.configuration;

import java.util.ArrayList;
import java.util.List;

public class PortConfig {
    public static final int BOOTSTRAP_PORT = 1234;
    private static final int NODES_NUMBER = 4;
    private static final int START_PORT = 1500;
    private static List<Integer> nodesPorts;

    static {
        nodesPorts = new ArrayList<>();
        for (int i = 0; i < NODES_NUMBER; i++) {
            nodesPorts.add( START_PORT + i * 1000);
        }
    }
    public static int getNodePort(int nodeID) {
        if (nodeID > 0 && nodeID <= nodesPorts.size()) {
            return nodesPorts.get(nodeID - 1);
        }

        throw new IllegalArgumentException("Invalid port number requested: " + nodeID);
    }
}
