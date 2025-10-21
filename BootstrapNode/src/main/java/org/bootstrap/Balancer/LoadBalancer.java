package org.bootstrap.Balancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancer {
    private static LoadBalancer instance;
    private List<Integer> nodes;
    private AtomicInteger nextNode;

    public static LoadBalancer getInstance(){
        if(instance == null)
            instance = new LoadBalancer();
        return instance;
    }
    private LoadBalancer() {
        this.nextNode = new AtomicInteger(0);
    }

    public void setNodes(List<Integer> nodes) {
        this.nodes = nodes;
    }

    public int getNextNode() {
        int nextIndex = nextNode.getAndIncrement() % nodes.size();
        return nodes.get(nextIndex);
    }
}