package org.project.validation;

import org.project.network.Node;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.spec.KeySpec;
import java.util.Base64;

public class UserValidator implements Validator {
    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String PASSPHRASE = "Khaled";
    private static final byte[] SALT = "SomeSalt".getBytes();
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;

    private static SecretKey getKeyFromPassphrase() throws Exception {
        KeySpec spec = new PBEKeySpec(PASSPHRASE.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
        byte[] tmp = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(tmp, ALGORITHM);
    }
    public boolean userValidation(String username, String password) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(Node.getUsersInfo()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts[0].equals(username)) {
                try {
                    if (encrypt(password).equals(parts[1]))
                        return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }
    public String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromPassphrase());
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    public String decrypt(String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromPassphrase());
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
        return new String(decryptedBytes);
    }
    @Override
    public void addToFile(String username, String password, String role) {
        try {
            String encryptedPassword = encrypt(password);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Node.getUsersInfo(), true))) {
                writer.write(username + ":" + encryptedPassword + ":" + role);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while adding the user to the file.");
        }
    }
}
