package org.project.authentication;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class RoleFactory {
    public static Roles getRole(String role,
                                DataInputStream receiver,
                                PrintWriter sender, ObjectOutputStream sendObject) {
        return switch (role) {
            case "user" -> new UserRole(receiver, sender ,sendObject);
            case "admin" -> new AdminRole(receiver, sender);
            default -> null;
        };
    }
}
