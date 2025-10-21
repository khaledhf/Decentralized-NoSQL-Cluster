package org.client.configuration;

import java.io.PrintWriter;

public class Utility {
    public static void sendMessage(PrintWriter sender, String message){
        sender.println(message);
        sender.flush();
    }
}
