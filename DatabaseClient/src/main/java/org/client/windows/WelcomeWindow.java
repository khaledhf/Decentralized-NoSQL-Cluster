package org.client.windows;

import org.client.communicator.BootstrapCommunicator;
import org.client.configuration.PortConfig;
import org.client.configuration.Utility;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class WelcomeWindow implements Window{
    private Scanner input = new Scanner(System.in);
    @Override
    public void popupWindow() {
        boolean isFinish = false;
        try {
        System.out.println("Welcome To Atypon Database!!\n1. Login\n2. Sign up\n3. Exit");
        int choice = input.nextInt();
            switch (choice) {
                case 1 -> login();
                case 2 -> signup();
                case 3 -> System.out.println("Exiting :)");
                default -> System.out.println("We have only Login and Signup :'(");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login() throws IOException {
        input.nextLine();
        System.out.println("Enter your username:");
        String username = input.nextLine();
        System.out.println("Enter your password:");
        String password = input.nextLine();
        int nodeID = BootstrapCommunicator.getInstance().getNodeID(username);
        if (nodeID < 0){
            System.out.println("Not Signed up !!"); return;
        }
        System.out.println(nodeID);
        Socket nodeSocket = new Socket(InetAddress.getLocalHost(), PortConfig.getNodePort(nodeID));
        BufferedReader receiver
                = new BufferedReader(new InputStreamReader(nodeSocket.getInputStream()));
        PrintWriter sender
                = new PrintWriter(nodeSocket.getOutputStream());
        Utility.sendMessage(sender,username);
        System.out.println(username);
        Utility.sendMessage(sender,password);
        String validationStatus = receiver.readLine();
        System.out.println(validationStatus);
        if (validationStatus.equals("Not Valid User!! try again")) {
            return;
        }
        String privilege = receiver.readLine();
        System.out.println(privilege);
        ObjectInputStream receiveObject =new ObjectInputStream(nodeSocket.getInputStream());
        Window newWindow = WindowsFactory.getWindow(privilege,receiver,sender, receiveObject);
        newWindow.popupWindow();
    }
    private void signup() throws IOException {
        input.nextLine();
        System.out.println("Enter your username:");
        String username= input.nextLine();
        if (!BootstrapCommunicator.getInstance().usernameValidator(username)){
            System.out.println("Taken Username");
            return;
        }
        System.out.println("Enter your password:");
        String password = input.nextLine();

        System.out.println("Enter your Role: admin/user");
        String role = input.nextLine().toLowerCase();
        BootstrapCommunicator.getInstance().signupNewUser(username, password, role);
        System.out.println("Signup Done Successfully Try to Login in now :)");
    }
}
