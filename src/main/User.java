package main;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;

public abstract class User {
    private String username;
    private String password;

    public User(String username) {
        this.username = username;
        this.password = "";
    }

    public User(String username, String password) {
        this.username = username;

        // hash password
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            this.password = hashPassword(password, salt);
        } catch (Exception e) {
            System.out.println("Error hashing password: " + e.getMessage());
        }
    }

    public String getUsername() {
        return this.username;
    }

    private String hashPassword(String rawPassword, byte[] salt) throws Exception {
        // use pbkdf2
        // citation: https://www.baeldung.com/java-password-hashing
        // create key
        KeySpec spec = new javax.crypto.spec.PBEKeySpec(rawPassword.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public String getPassword() {
        return this.password;
    }

    public boolean comparePasswords(String userPassword) {
        try {
            String[] passwordParts = this.password.split(":");
            byte[] salt = Base64.getDecoder().decode(passwordParts[0]);
            String hashedPassword = hashPassword(userPassword, salt);

            if (hashedPassword.equals(this.password)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return false;
        }
    }

    public boolean setNewPassword(String newPassword) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            this.password = hashPassword(newPassword, salt);
            return true;
        } catch (Exception e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return false;
        }
    }
}