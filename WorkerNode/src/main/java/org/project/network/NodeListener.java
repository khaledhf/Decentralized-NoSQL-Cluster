package org.project.network;

import org.project.broadcast.BroadcastSender;
import org.project.configuration.Query;
import org.project.storage.handlers.CRUDHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class NodeListener implements Runnable{
    private ServerSocket affinityServer;
    private DataInputStream receiver;
    private ObjectInputStream receiveObject;
    private PrintWriter sender;
    private List<String> queryName;
    public NodeListener(ServerSocket affinityServer) {
        this.affinityServer = affinityServer;
    }
    @Override
    public void run() {
        while (true){
        try {
            Socket affinityNode = affinityServer.accept();
            this.receiver
                    = new DataInputStream(affinityNode.getInputStream());
            this.sender
                    = new PrintWriter(affinityNode.getOutputStream());
            this.receiveObject
                    =new ObjectInputStream(affinityNode.getInputStream());

            Query query = Query.valueOf(receiver.readLine());
            queryName = (List<String>) receiveObject.readObject();
            affinityAction(query);
            receiver.close();
            sender.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        }
    }
    private void affinityAction(Query query){
        switch (query){
            case UpdateData:
                updateDataHandler();
            case DeleteData:
                deleteDataHandler();
            case DeleteAllData:
                deleteAllDataHandler();
    }
}
    private void updateDataHandler(){
        CRUDHandler crudHandler = new CRUDHandler(queryName.get(0), queryName.get(1), queryName.get(2));
        crudHandler.updateData(queryName.get(3), queryName.get(4));
        BroadcastSender.broadcastMessage(queryName,Query.UpdateData);
        System.out.println("Send To Affinity Node and Update Data" +
                " Have Been Done Successfully");
    }
    private void deleteDataHandler(){
        CRUDHandler crudHandler = new CRUDHandler(queryName.get(0), queryName.get(1), queryName.get(2));
        crudHandler.deleteData(queryName.get(3));
        BroadcastSender.broadcastMessage(queryName,Query.DeleteData);
        System.out.println("Send To Affinity Node and Delete Data" +
                " Have Been Done Successfully");
    }
    private void deleteAllDataHandler(){
        CRUDHandler crudHandler = new CRUDHandler(queryName.get(0), queryName.get(1), queryName.get(2));
        crudHandler.deleteAllData();
        BroadcastSender.broadcastMessage(queryName,Query.DeleteAllData);
        System.out.println("Send To Affinity Node and Delete All Data" +
                " Have Been Done Successfully");
    }
}
