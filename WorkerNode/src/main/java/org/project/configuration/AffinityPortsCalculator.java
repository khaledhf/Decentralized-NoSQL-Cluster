package org.project.configuration;

import java.util.ArrayList;
import java.util.List;

public class AffinityPortsCalculator {

    private static final int START_PORT = 1600;
    private static List<Integer> affinityPorts;

    static {
        affinityPorts = new ArrayList<>();
        for (int i = 0; i < DBConfig.NODES_NUMBER; i++) {
            affinityPorts.add( START_PORT + i * 1000);
        }
    }
    public static int getAffinityPort(int number) {
        if (number > 0 && number <= affinityPorts.size()) {
            return affinityPorts.get(number - 1);
        }
        throw new IllegalArgumentException("Invalid port number requested: " + number);
    }
}
