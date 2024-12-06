package com.example.demo.service;

import com.example.demo.User;
import com.example.demo.LoginCredential;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SecretKey secretKey;

    @Autowired
    public UserService(UserRepository userRepository) throws Exception {
        this.userRepository = userRepository;
        String encodedKey = System.getenv("SECRET_KEY");

        if (encodedKey == null) {
            throw new RuntimeException("SECRET_KEY environment variable not set");
        }

        // Decode the Base64-encoded key
        this.secretKey = EncryptionUtil.getKeyFromString(encodedKey);
    }

    // LOOK INTO HOW TO USE THIS PROPERLY FOR MORE PROTECTION

//    public User getUserById(UUID userId, UUID requestingUserId) throws Exception {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        if (!user.getId().equals(requestingUserId)) {
//            throw new AccessDeniedException("You do not have permission to access this resource");
//        }
//        decryptUser(user);
//        return user;
//    }

    public List<User> getAllUsers() throws Exception {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            decryptUser(user);
        }
        return users;
    }

    public User getUserById(UUID id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        decryptUser(user);
        return user;
    }

    public User createUser(User user) throws Exception {
        encryptUser(user);
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User userDetails) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
            user.setName(EncryptionUtil.encrypt(userDetails.getName(), secretKey));
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            user.setEmail(EncryptionUtil.encrypt(userDetails.getEmail(), secretKey));
        }
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(EncryptionUtil.encrypt(userDetails.getPassword(), secretKey));
        }
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    private void encryptUser(User user) throws Exception {
        user.setName(EncryptionUtil.encrypt(user.getName(), secretKey));
        user.setEmail(EncryptionUtil.encrypt(user.getEmail(), secretKey));
        user.setPassword(EncryptionUtil.encrypt(user.getPassword(), secretKey));
        for (LoginCredential credential : user.getLoginCredentials()) {
            credential.setUsername(EncryptionUtil.encrypt(credential.getUsername(), secretKey));
            credential.setPassword(EncryptionUtil.encrypt(credential.getPassword(), secretKey));
            credential.setWebsite(EncryptionUtil.encrypt(credential.getWebsite(), secretKey));
        }
    }

    private void decryptUser(User user) throws Exception {
        user.setName(EncryptionUtil.decrypt(user.getName(), secretKey));
        user.setEmail(EncryptionUtil.decrypt(user.getEmail(), secretKey));
        user.setPassword(EncryptionUtil.decrypt(user.getPassword(), secretKey));
        for (LoginCredential credential : user.getLoginCredentials()) {
            credential.setUsername(EncryptionUtil.decrypt(credential.getUsername(), secretKey));
            credential.setPassword(EncryptionUtil.decrypt(credential.getPassword(), secretKey));
            credential.setWebsite(EncryptionUtil.decrypt(credential.getWebsite(), secretKey));
        }
    }
}