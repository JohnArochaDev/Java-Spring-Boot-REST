package com.example.demo.service;

import com.example.demo.User;
import com.example.demo.LoginCredential;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.EncryptionUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) throws Exception {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // DIS HURT STUFF
        String encodedKey = System.getenv("SECRET_KEY");

        if (encodedKey == null) {
            throw new RuntimeException("SECRET_KEY environment variable not set");
        }

        // Decode the Base64-encoded key
        this.secretKey = EncryptionUtil.getKeyFromString(encodedKey);
    }

    public User findByEmail(String email) throws Exception {
        String encryptedEmail = EncryptionUtil.encrypt(email, secretKey);
        return userRepository.findByUsername(encryptedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String encryptedEmail;
        try {
            encryptedEmail = EncryptionUtil.encrypt(email, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        User user = userRepository.findByUsername(encryptedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        // Return a UserDetails object with the hashed password
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword()) // The password is already hashed
            .authorities(user.getAuthorities())
            .accountExpired(!user.isAccountNonExpired())
            .accountLocked(!user.isAccountNonLocked())
            .credentialsExpired(!user.isCredentialsNonExpired())
            .disabled(!user.isEnabled())
            .build();
    }

    public List<User> getAllUsers() throws Exception {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setUsername(EncryptionUtil.decrypt(user.getUsername(), secretKey));
            decryptLoginCredentials(user);
        }
        return users;
    }

    private void decryptLoginCredentials(User user) throws Exception {
        for (LoginCredential credential : user.getLoginCredentials()) {
            credential.setUsername(EncryptionUtil.decrypt(credential.getUsername(), secretKey));
            credential.setPassword(EncryptionUtil.decrypt(credential.getPassword(), secretKey));
            credential.setWebsite(EncryptionUtil.decrypt(credential.getWebsite(), secretKey));
        }
    }

    public User getUserById(UUID id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        decryptUser(user);
        return user;
    }

    public User createUser(User user) throws Exception {

        String encryptedEmail = EncryptionUtil.encrypt(user.getUsername().toLowerCase(), secretKey);

        String hashedPassword = passwordEncoder.encode(user.getPassword());

        user.setUsername(encryptedEmail);
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User updateUser(UUID id, User userDetails) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
            user.setName(EncryptionUtil.encrypt(userDetails.getName(), secretKey));
        }
        if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            user.setUsername(EncryptionUtil.encrypt(userDetails.getUsername(), secretKey));
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
        user.setUsername(EncryptionUtil.encrypt(user.getUsername(), secretKey));
        user.setPassword(EncryptionUtil.encrypt(user.getPassword(), secretKey));
        for (LoginCredential credential : user.getLoginCredentials()) {
            credential.setUsername(EncryptionUtil.encrypt(credential.getUsername(), secretKey));
            credential.setPassword(EncryptionUtil.encrypt(credential.getPassword(), secretKey));
            credential.setWebsite(EncryptionUtil.encrypt(credential.getWebsite(), secretKey));
        }
    }

    private void decryptUser(User user) throws Exception {
        user.setName(user.getName());
        user.setUsername(EncryptionUtil.decrypt(user.getUsername(), secretKey));
        user.setPassword(user.getPassword());
        user.setId(user.getId());
        for (LoginCredential credential : user.getLoginCredentials()) {
            credential.setUsername(EncryptionUtil.decrypt(credential.getUsername(), secretKey));
            credential.setPassword(EncryptionUtil.decrypt(credential.getPassword(), secretKey));
            credential.setWebsite(EncryptionUtil.decrypt(credential.getWebsite(), secretKey));
        }
    }

    public boolean authenticateUser(String email, String password) throws Exception {
        // Encrypt email and password
        String encryptedEmail = EncryptionUtil.encrypt(email, secretKey);

        Optional<User> userOptional = userRepository.findByUsername(encryptedEmail);

        // Log the result of the user search
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            return passwordEncoder.matches(password, user.getPassword());
        }

        return false;
    }
}