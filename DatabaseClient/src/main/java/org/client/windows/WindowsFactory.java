package org.client.windows;

import java.io.*;

public class WindowsFactory {
    public static Window getWindow(String role,
                                   BufferedReader receiver,
                                   PrintWriter sender , ObjectInputStream receiveObject) {
        switch (role){
            case "user":
                return new UserWindow(receiver,sender, receiveObject);
            case "admin":
                return new AdminWindow(receiver,sender);
        }
        return null;
    }
}
