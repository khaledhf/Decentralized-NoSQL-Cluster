package org.project.validation;

import org.project.network.Node;

import javax.crypto.Cipher;
import java.io.*;
import java.util.Base64;

public interface Validator {
    boolean userValidation(String username, String password) throws IOException;
    String encrypt(String plaintext) throws Exception;
    String decrypt(String ciphertext) throws Exception ;
    void addToFile(String username, String password, String role);
}
