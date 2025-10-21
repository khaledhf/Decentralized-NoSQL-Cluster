package org.project.network;

import org.project.authentication.PrivilegeCheck;
import org.project.authentication.RoleFactory;
import org.project.authentication.Roles;
import org.project.validation.UserValidator;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserListener implements Runnable{
    private Socket user;
    private DataInputStream receiver;
    private PrintWriter sender;
    private  ObjectOutputStream sendObject;
    private String username;
    private String password;
    private UserValidator userValidator;


    public UserListener(Socket user) {
       this.user = user;
       this.userValidator = new UserValidator();
    }

    @Override
    public void run() {
        try {
            this.receiver = new DataInputStream(user.getInputStream());
            this.sender = new PrintWriter(user.getOutputStream());
            username = receiver.readLine();
            password = receiver.readLine();
            if (!userValidator.userValidation(username,password)){
                sendMessage("Not Valid User!! try again");
            }
            else{
                sendMessage("Valid User :)");
            }
            System.out.println(username);
                PrivilegeCheck privilegeCheck =
                        new PrivilegeCheck(Node.getUsersInfo().getPath());
                sendMessage(privilegeCheck.getPrivilege(username));
            this.sendObject = new ObjectOutputStream(user.getOutputStream());
            Roles user =RoleFactory.getRole(privilegeCheck.getPrivilege(username),receiver,sender,sendObject);
                user.start();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void sendMessage(String message){
        sender.println(message);
        sender.flush();
    }
}
