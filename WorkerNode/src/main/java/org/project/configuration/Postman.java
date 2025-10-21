package org.project.configuration;

import java.io.PrintWriter;

public class Postman {
    public static void sendMessage(PrintWriter sender, String message){
        sender.println(message);
        sender.flush();
    }
}
