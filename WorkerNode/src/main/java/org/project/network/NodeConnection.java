package org.project.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class NodeConnection implements AutoCloseable {
    private Socket node;
    private DataInputStream receiver;
    private PrintWriter sender;
    private ObjectOutputStream sendObject;

    public NodeConnection(int port) throws IOException {
        node = new Socket(InetAddress.getLocalHost(), port);
        receiver = new DataInputStream(node.getInputStream());
        sender = new PrintWriter(node.getOutputStream());
        sendObject = new ObjectOutputStream(node.getOutputStream());
    }

    public void sendMessage(String message) {
        sender.println(message);
        sender.flush();
    }

    public void sendObject(Object obj) throws IOException {
        sendObject.writeObject(obj);
        sendObject.flush();
    }

    public String receiveMessage() throws IOException {
        return receiver.readLine();
    }

    @Override
    public void close() throws Exception {
        node.close();
    }
}
