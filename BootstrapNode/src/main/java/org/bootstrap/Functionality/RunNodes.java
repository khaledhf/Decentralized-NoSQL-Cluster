package org.bootstrap.Functionality;

import java.io.IOException;

public class RunNodes {
    public static void run(){
    for (int i = 0 ; i < FileConfig.NODES_NUMBER ; i++){
        try {
            Runtime.getRuntime().exec("docker run khaledhf/node"+(i+1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
}
