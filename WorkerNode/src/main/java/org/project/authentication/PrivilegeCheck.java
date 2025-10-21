package org.project.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrivilegeCheck {
    private final String filePath;
    private Map<String, String> userPrivileges = new HashMap<>();

    public PrivilegeCheck(String filePath) {
        this.filePath = filePath;
        loadPrivileges();
    }

    private void loadPrivileges() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    userPrivileges.put(parts[0],  parts[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getPrivilege(String username) {
        String privilege = userPrivileges.get(username);
        return privilege != null ? privilege : "unknown";
    }

    public boolean isAdmin(String username) {
        return "admin".equalsIgnoreCase(getPrivilege(username));
    }

    public boolean isUser(String username) {
        return "user".equalsIgnoreCase(getPrivilege(username));
    }


}
