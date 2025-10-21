package org.bootstrap.Functionality;

import java.util.ArrayList;
import java.util.List;

public class NodePorts {
    private static final int START_PORT = 1700;
    private static List<Integer> nodesPorts;

    static {
        nodesPorts = new ArrayList<>();
        for (int i = 0; i < FileConfig.NODES_NUMBER; i++) {
            nodesPorts.add( START_PORT + i * 1000);
        }
    }
    public static int getNodePort(int number) {
        if (number > 0 && number <= nodesPorts.size()) {
            return nodesPorts.get(number - 1);
        }
        throw new IllegalArgumentException("Invalid port number requested: " + number);
    }

    public static List<Integer> getNodesPorts() {
        return nodesPorts;
    }
}
