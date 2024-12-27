package com.example.demo.service;

import com.example.demo.LoginCredential;
import com.example.demo.User;
import com.example.demo.repository.LoginCredentialRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.UUID;

@Service
public class LoginCredentialService {
    private final LoginCredentialRepository loginCredentialRepository;
    private final UserRepository userRepository;
    private final SecretKey secretKey;

    @Autowired
    public LoginCredentialService(LoginCredentialRepository loginCredentialRepository, UserRepository userRepository) throws Exception {
        this.loginCredentialRepository = loginCredentialRepository;
        this.userRepository = userRepository;
        String encodedKey = System.getenv("SECRET_KEY");

        if (encodedKey == null) {
            throw new RuntimeException("SECRET_KEY environment variable not set");
        }

        // Decode the Base64-encoded key
        this.secretKey = EncryptionUtil.getKeyFromString(encodedKey);
    }

    public List<LoginCredential> getAllLoginCredentials() throws Exception {
        List<LoginCredential> loginCredentials = loginCredentialRepository.findAll();
        for (LoginCredential loginCredential : loginCredentials) {
            decryptLoginCredential(loginCredential);
        }
        return loginCredentials;
    }

    // This route is for testing, DELETE IT LATER IT'S A SECURITY RISK
    public LoginCredential getLoginCredentialById(UUID id) throws Exception {
        LoginCredential loginCredential = loginCredentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));
        decryptLoginCredential(loginCredential);
        return loginCredential;
    }

    public LoginCredential createLoginCredential(UUID userId, LoginCredential loginCredential) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Make the email lower case
        loginCredential.setWebsite(loginCredential.getWebsite().toLowerCase());

        // Encrypt the login credential fields
        encryptLoginCredential(loginCredential);

        // Set the user for the login credential
        loginCredential.setUser(user);

        // Add the login credential to the user's loginCredentials list
        user.getLoginCredentials().add(loginCredential);

        // Save the login credential first
        loginCredentialRepository.save(loginCredential);

        // Save the user with the updated credentials list
        userRepository.save(user);

        return loginCredential;
    }

    public LoginCredential updateLoginCredential(UUID id, LoginCredential loginCredentialDetails) throws Exception {
        LoginCredential loginCredential = loginCredentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));

        if (loginCredentialDetails.getUsername() != null && !loginCredentialDetails.getUsername().isEmpty()) {
            loginCredential.setUsername(EncryptionUtil.encrypt(loginCredentialDetails.getUsername(), secretKey));
        }

        if (loginCredentialDetails.getPassword() != null && !loginCredentialDetails.getPassword().isEmpty()) {
            loginCredential.setPassword(EncryptionUtil.encrypt(loginCredentialDetails.getPassword(), secretKey));
        }

        if (loginCredentialDetails.getWebsite() != null && !loginCredentialDetails.getWebsite().isEmpty()) {
            loginCredential.setWebsite(EncryptionUtil.encrypt(loginCredentialDetails.getWebsite(), secretKey));
        }

        return loginCredentialRepository.save(loginCredential);
    }

    public void deleteLoginCredentialById(UUID id) throws Exception {
        LoginCredential loginCredential = loginCredentialRepository.findById(id).orElseThrow(() -> new RuntimeException("Credential not found"));
        loginCredentialRepository.delete(loginCredential);
    }

    private void encryptLoginCredential(LoginCredential loginCredential) throws Exception {
        loginCredential.setUsername(EncryptionUtil.encrypt(loginCredential.getUsername(), secretKey));
        loginCredential.setPassword(EncryptionUtil.encrypt(loginCredential.getPassword(), secretKey));
        loginCredential.setWebsite(EncryptionUtil.encrypt(loginCredential.getWebsite(), secretKey));
    }

    private void decryptLoginCredential(LoginCredential loginCredential) throws Exception {
        loginCredential.setUsername(EncryptionUtil.decrypt(loginCredential.getUsername(), secretKey));
        loginCredential.setPassword(EncryptionUtil.decrypt(loginCredential.getPassword(), secretKey));
        loginCredential.setWebsite(EncryptionUtil.decrypt(loginCredential.getWebsite(), secretKey));
    }
}